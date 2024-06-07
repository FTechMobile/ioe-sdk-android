package ai.ftech.ioesdk.data.service

import ai.ftech.ioesdk.data.model.initrecord.InitRecordIOERequest
import ai.ftech.ioesdk.data.model.initrecord.InitRecordIOEResponse
import ai.ftech.ioesdk.data.source.remote.base.BaseApiResponse
import ai.ftech.ioesdk.data.source.remote.base.IApiService
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IOEInitRecordService : IApiService {
    @POST("/auth/sdk/init")
    fun initRecord(@Body body: InitRecordIOERequest
    ): Call<InitRecordIOEResponse>
}
