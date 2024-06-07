package ai.ftech.ioesdk.data.model.stoprecord

import ai.ftech.ioesdk.data.source.remote.base.BaseApiRequest
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StopRecordIOERequest : BaseApiRequest() {
    @SerializedName("request_id")
    @Expose
    var requestId: String? = null

    @SerializedName("file")
    @Expose
    var file: String? = null
}
