package ai.ftech.ioesdk.data.model.initrecord

import ai.ftech.ioesdk.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InitRecordIOEResponse : BaseApiResponse() {

    @SerializedName("data")
    @Expose
    var data: InitRecordIOEData? = null

    @SerializedName("ts")
    @Expose
    var ts: String? = null

    class InitRecordIOEData {
        @SerializedName("token")
        @Expose
        var token: String? = null

        @SerializedName("service")
        @Expose
        var service: Service? = null

        class Service {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("code")
            @Expose
            var code: String? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("link")
            @Expose
            var link: String? = null

            @SerializedName("image")
            @Expose
            var image: String? = null

            @SerializedName("info")
            @Expose
            var info: String? = null

            @SerializedName("status")
            @Expose
            var status: Int? = null
        }
    }
}
