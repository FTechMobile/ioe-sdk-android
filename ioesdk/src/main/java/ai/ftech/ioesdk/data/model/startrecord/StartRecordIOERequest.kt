package ai.ftech.ioesdk.data.model.startrecord

import ai.ftech.ioesdk.data.source.remote.base.BaseApiRequest
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StartRecordIOERequest : BaseApiRequest() {
    @SerializedName("reference_text")
    @Expose
    var referenceText: String? = null

    @SerializedName("english_accent")
    @Expose
    var englishAccent: String? = null

    @SerializedName("extra_data")
    @Expose
    var extraData: String? = null
}
