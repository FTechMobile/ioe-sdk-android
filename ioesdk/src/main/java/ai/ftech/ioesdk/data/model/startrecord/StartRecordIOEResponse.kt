package ai.ftech.ioesdk.data.model.startrecord

import ai.ftech.ioesdk.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StartRecordIOEResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: StartRecordIOEData? = null

    class StartRecordIOEData {
        @SerializedName("request_id")
        @Expose
        var requestId: String? = null
    }
}
