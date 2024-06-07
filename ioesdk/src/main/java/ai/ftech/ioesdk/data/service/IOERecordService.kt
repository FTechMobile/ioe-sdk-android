package ai.ftech.ioesdk.data.service

import ai.ftech.ioesdk.data.model.startrecord.StartRecordIOERequest
import ai.ftech.ioesdk.data.model.startrecord.StartRecordIOEResponse
import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse
import ai.ftech.ioesdk.data.source.remote.base.IApiService
import okhttp3.MultipartBody
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface IOERecordService : IApiService {
    @POST("/ioe/start_stream_v2")
    fun startRecord(@Body body: StartRecordIOERequest): Call<StartRecordIOEResponse>

    @Multipart
    @POST("/ioe/stop_stream")
    fun stopRecord(
        @Part file: MultipartBody.Part,
        @Query("request_id") requestId: String,
    ): Call<StopRecordIOEResponse>
}
