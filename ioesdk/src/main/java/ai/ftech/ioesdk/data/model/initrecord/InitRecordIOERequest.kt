package ai.ftech.ioesdk.data.model.initrecord

import ai.ftech.ioesdk.data.source.remote.base.BaseApiRequest
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InitRecordIOERequest(
    @SerializedName("app_id")
    @Expose
    var appId: String? = null,

    @SerializedName("secret_key")
    @Expose
    var secretKey: String? = null,

) : BaseApiRequest()
