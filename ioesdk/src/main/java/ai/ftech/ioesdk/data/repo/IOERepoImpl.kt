package ai.ftech.ioesdk.data.repo

import ai.ftech.ioesdk.R
import ai.ftech.ioesdk.base.repo.BaseRepo
import ai.ftech.ioesdk.common.getAppString
import ai.ftech.ioesdk.data.converter.InitRecordIOEResponseConvertToInitRecord
import ai.ftech.ioesdk.data.converter.StartRecordIOEResponseConvertToStartRecord
import ai.ftech.ioesdk.data.model.initrecord.InitRecordIOERequest
import ai.ftech.ioesdk.data.model.startrecord.StartRecordIOERequest
import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse
import ai.ftech.ioesdk.data.service.IOEInitRecordService
import ai.ftech.ioesdk.data.service.IOERecordService
import ai.ftech.ioesdk.data.source.remote.base.invokeFIOEService
import ai.ftech.ioesdk.data.source.remote.base.invokeInitSDKFEkycService
import ai.ftech.ioesdk.domain.APIException
import ai.ftech.ioesdk.domain.APIException.Companion.BAD_REQUEST
import ai.ftech.ioesdk.domain.APIException.Companion.CANT_INIT_SDK
import ai.ftech.ioesdk.domain.APIException.Companion.DONT_CONNECT_TO_SERVER
import ai.ftech.ioesdk.domain.APIException.Companion.INTERNAL_SERVER_ERROR
import ai.ftech.ioesdk.domain.APIException.Companion.SUCCESS
import ai.ftech.ioesdk.domain.model.InitRecord
import ai.ftech.ioesdk.domain.model.StartRecord
import ai.ftech.ioesdk.domain.repo.IIOERepo
import ai.ftech.ioesdk.presentation.AppPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class IOERepoImpl : BaseRepo(), IIOERepo {
    companion object {
        private const val PART_FILE = "file"
    }

    override fun initRecord(appId: String, secretKey: String): Boolean {
        val service = invokeInitSDKFEkycService(IOEInitRecordService::class.java)

        val request = InitRecordIOERequest().apply {
            this.appId = appId
            this.secretKey = secretKey
        }

        val data = service.initRecord(request).execute()

        when (data.body()?.code) {
            SUCCESS -> {
                AppPreferences.token = data.body()?.data?.token
                return true
            }
            else -> {
                throw APIException(CANT_INIT_SDK, getAppString(R.string.ioe_init_not_success))
            }
        }
    }

    override fun startRecord(referenceText: String, englishAccent: String, extraData: String?): StartRecord {
        val service = invokeFIOEService(IOERecordService::class.java)

        val request = StartRecordIOERequest().apply {
            this.referenceText = referenceText
            this.englishAccent = englishAccent
            this.extraData = extraData
        }
        val data = service.startRecord(request).execute()
        if (data.isSuccessful) {
            when (data.body()?.code) {
                SUCCESS -> {
                    return StartRecordIOEResponseConvertToStartRecord().convert(data.body())
                }
                INTERNAL_SERVER_ERROR -> {
                    throw APIException(INTERNAL_SERVER_ERROR, getAppString(R.string.ioe_internal_server_error))
                }
                DONT_CONNECT_TO_SERVER -> {
                    throw APIException(DONT_CONNECT_TO_SERVER, getAppString(R.string.ioe_dont_connect_to_server))
                }
                else -> {
                    throw APIException(data.body()?.code ?: BAD_REQUEST, data.body()?.message ?: getAppString(R.string.ioe_bad_request))
                }
            }
        } else {
            throw APIException(DONT_CONNECT_TO_SERVER, getAppString(R.string.ioe_dont_connect_to_server))
        }
    }

    override fun stopRecord(file: String, requestId: String): StopRecordIOEResponse? {
        val service = invokeFIOEService(IOERecordService::class.java)

        val part = convertFileToMultipart(file)
        val data = service.stopRecord(part, requestId).execute()
        if (data.isSuccessful) {
            when (data.body()?.code) {
                SUCCESS -> {
                    return data.body()
                }
                INTERNAL_SERVER_ERROR -> {
                    throw APIException(INTERNAL_SERVER_ERROR, getAppString(R.string.ioe_internal_server_error))
                }
                DONT_CONNECT_TO_SERVER -> {
                    throw APIException(DONT_CONNECT_TO_SERVER, getAppString(R.string.ioe_dont_connect_to_server))
                }
                else -> {
                    throw APIException(data.body()?.code ?: BAD_REQUEST, data.body()?.message ?: getAppString(R.string.ioe_bad_request))
                }
            }
        } else {
            throw APIException(DONT_CONNECT_TO_SERVER, getAppString(R.string.ioe_dont_connect_to_server))
        }
    }

    private fun convertFileToMultipart(absolutePath: String): MultipartBody.Part {
        val file = File(absolutePath)
        return MultipartBody.Part.createFormData(PART_FILE, file.name, file.asRequestBody())
    }
}
