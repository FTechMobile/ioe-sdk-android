package ai.ftech.ioesdk.utils

import ai.ftech.ioesdk.base.extension.getApplication
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    private const val IOE_AUDIO_RECORDER_FOLDER = "IOEAudioRecorder"
    private const val IOE_AUDIO_RECORDER_FILE_HEAD = "record_"
    private const val IOE_AUDIO_RECORDER_FILE_TYPE = ".wav"
    fun generateNewFileRecord(): File? {
        val fileDir = getApplication()?.filesDir ?: return null
        val folderRecord = File(fileDir, IOE_AUDIO_RECORDER_FOLDER)
        if (!folderRecord.exists()) {
            folderRecord.mkdirs()
        }
        return try {
            val fileRecordName =
                IOE_AUDIO_RECORDER_FILE_HEAD + System.currentTimeMillis() + IOE_AUDIO_RECORDER_FILE_TYPE
            val fileRecord = File(folderRecord.path, fileRecordName)
            if (!fileRecord.exists()) {
                fileRecord.createNewFile()
            }
            fileRecord
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    fun deleteFile(pathFile: String) : Boolean = File(pathFile).delete()
}
