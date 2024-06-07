package ai.ftech.ioesdk.speech

import java.io.File

interface IAudioRecorder {
    fun start()
    fun stop()
    fun registerRecordingListener(listener : IRecordingListener)

    interface IRecordingListener {
        fun onStart()
        fun onRecording()
        fun onComplete(fileRecord: File)
        fun onFail(reason: String)
    }
}
