package ai.ftech.ioesdk.publish

import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse
import ai.ftech.ioesdk.domain.APIException

interface IFTechRecordingCallback {
    fun onStart()
    fun onRecording()
    fun onFail(error: APIException)
    fun onComplete(result: StopRecordIOEResponse)
}
