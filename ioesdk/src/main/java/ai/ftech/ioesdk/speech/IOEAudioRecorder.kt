package ai.ftech.ioesdk.speech

import ai.ftech.ioesdk.R
import ai.ftech.ioesdk.common.getAppString
import ai.ftech.ioesdk.utils.FileUtils
import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.konovalov.vad.yamnet.Vad
import com.konovalov.vad.yamnet.VadYamnet
import com.konovalov.vad.yamnet.config.FrameSize
import com.konovalov.vad.yamnet.config.Mode
import com.konovalov.vad.yamnet.config.SampleRate
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread
import kotlin.experimental.and

@SuppressLint("MissingPermission")
internal class IOEAudioRecorder(context: Context) : IAudioRecorder {

    private var vad: VadYamnet = Vad.builder()
        .setContext(context)
        .setSampleRate(SampleRate.SAMPLE_RATE_16K)
        .setFrameSize(FrameSize.FRAME_SIZE_243)
        .setMode(Mode.NORMAL)
        .build()

    private var recorder: AudioRecord? = null
    private var isRecording = false

    private var recordingThread: Thread? = null

    private var fileRecord: File? = null
    private var mListenerRecording: IAudioRecorder.IRecordingListener? = null

    override fun start() {
        mListenerRecording?.onStart()
        fileRecord = FileUtils.generateNewFileRecord()

        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            RECORDER_SAMPLE_RATE, RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING, BUFFER_ELEMENTS_2_REC
        )

        recorder?.startRecording()
        isRecording = true
        mListenerRecording?.onRecording()
        recordingThread = thread(true) {
            writeAudioDataToFile()
        }
    }

    override fun registerRecordingListener(listener: IAudioRecorder.IRecordingListener) {
        mListenerRecording = listener
    }

    override fun stop() {
        recorder?.run {
            isRecording = false
            stop()
            release()
            recordingThread = null
            recorder = null
            evaluateSpeechAudioRecord()
        }
    }

    private fun evaluateSpeechAudioRecord() {
        if (fileRecord != null) {
//            val detectSpeed = vad.classifyAudio(fileRecord!!.readBytes())
//            val hasSpeech = if (detectSpeed.label == LABEL_SPEECH) {
//                detectSpeed.score >= 0.5
//            } else {
//                false
//            }
//            Log.d("IOEAudioRecorder", "detect: ${detectSpeed.label}: ${detectSpeed.score}")
//            if (hasSpeech) {
                mListenerRecording?.onComplete(fileRecord!!)
//            } else {
//                mListenerRecording?.onFail(getAppString(R.string.message_no_detect_speech))
//                FileUtils.deleteFile(fileRecord!!.absolutePath)
//            }
        } else {
            mListenerRecording?.onFail(getAppString(R.string.message_error_record_process))
        }
    }

    private fun short2byte(sData: ShortArray): ByteArray {
        val arrSize = sData.size
        val bytes = ByteArray(arrSize * 2)
        for (i in 0 until arrSize) {
            bytes[i * 2] = (sData[i] and 0x00FF).toByte()
            bytes[i * 2 + 1] = (sData[i].toInt() shr 8).toByte()
            sData[i] = 0
        }
        return bytes
    }

    private fun writeAudioDataToFile() {
        val sData = ShortArray(BUFFER_ELEMENTS_2_REC)
        val os: FileOutputStream?
        try {
            os = FileOutputStream(fileRecord)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            mListenerRecording?.onFail(getAppString(R.string.message_error_record_process))
            fileRecord = null
            return
        }

        val data = arrayListOf<Byte>()

        for (byte in wavFileHeader()) {
            data.add(byte)
        }

        while (isRecording) {
            recorder?.read(sData, 0, BUFFER_ELEMENTS_2_REC)
            try {
                val bData = short2byte(sData)
                for (byte in bData)
                    data.add(byte)
            } catch (e: IOException) {
                e.printStackTrace()
                mListenerRecording?.onFail(getAppString(R.string.message_error_record_process))
                fileRecord = null
                return
            }
        }

        updateHeaderInformation(data)

        os.write(data.toByteArray())

        try {
            os.flush()
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
            mListenerRecording?.onFail(getAppString(R.string.message_error_record_process))
            fileRecord = null
        }
    }

    private fun wavFileHeader(): ByteArray {
        val headerSize = 44
        val header = ByteArray(headerSize)

        header[0] = 'R'.code.toByte() // RIFF/WAVE header
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()

        header[4] = (0 and 0xff).toByte() // Size of the overall file, 0 because unknown
        header[5] = (0 shr 8 and 0xff).toByte()
        header[6] = (0 shr 16 and 0xff).toByte()
        header[7] = (0 shr 24 and 0xff).toByte()

        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()

        header[12] = 'f'.code.toByte() // 'fmt ' chunk
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()

        header[16] = 16 // Length of format data
        header[17] = 0
        header[18] = 0
        header[19] = 0

        header[20] = 1 // Type of format (1 is PCM)
        header[21] = 0

        header[22] = NUMBER_CHANNELS.toByte()
        header[23] = 0

        header[24] = (RECORDER_SAMPLE_RATE and 0xff).toByte() // Sampling rate
        header[25] = (RECORDER_SAMPLE_RATE shr 8 and 0xff).toByte()
        header[26] = (RECORDER_SAMPLE_RATE shr 16 and 0xff).toByte()
        header[27] = (RECORDER_SAMPLE_RATE shr 24 and 0xff).toByte()

        header[28] =
            (BYTE_RATE and 0xff).toByte() // Byte rate = (Sample Rate * BitsPerSample * Channels) / 8
        header[29] = (BYTE_RATE shr 8 and 0xff).toByte()
        header[30] = (BYTE_RATE shr 16 and 0xff).toByte()
        header[31] = (BYTE_RATE shr 24 and 0xff).toByte()

        header[32] = (NUMBER_CHANNELS * BITS_PER_SAMPLE / 8).toByte() //  16 Bits stereo
        header[33] = 0

        header[34] = BITS_PER_SAMPLE.toByte() // Bits per sample
        header[35] = 0

        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()

        header[40] = (0 and 0xff).toByte() // Size of the data section.
        header[41] = (0 shr 8 and 0xff).toByte()
        header[42] = (0 shr 16 and 0xff).toByte()
        header[43] = (0 shr 24 and 0xff).toByte()

        return header
    }

    private fun updateHeaderInformation(data: ArrayList<Byte>) {
        val fileSize = data.size
        val contentSize = fileSize - 44

        data[4] = (fileSize and 0xff).toByte() // Size of the overall file
        data[5] = (fileSize shr 8 and 0xff).toByte()
        data[6] = (fileSize shr 16 and 0xff).toByte()
        data[7] = (fileSize shr 24 and 0xff).toByte()

        data[40] = (contentSize and 0xff).toByte() // Size of the data section.
        data[41] = (contentSize shr 8 and 0xff).toByte()
        data[42] = (contentSize shr 16 and 0xff).toByte()
        data[43] = (contentSize shr 24 and 0xff).toByte()
    }

    companion object {
        const val RECORDER_SAMPLE_RATE = 16000
        const val RECORDER_CHANNELS: Int = AudioFormat.CHANNEL_IN_MONO
        const val RECORDER_AUDIO_ENCODING: Int = AudioFormat.ENCODING_PCM_16BIT
        const val BITS_PER_SAMPLE: Short = 16
        const val NUMBER_CHANNELS: Short = 1
        const val BYTE_RATE = RECORDER_SAMPLE_RATE * NUMBER_CHANNELS * 16 / 8
        private const val LABEL_SPEECH = "Speech"
        private const val BUFFER_ELEMENTS_2_REC = 1024
    }
}
