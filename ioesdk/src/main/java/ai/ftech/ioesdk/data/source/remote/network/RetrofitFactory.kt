package ai.ftech.ioesdk.data.source.remote.network

import ai.ftech.ioesdk.domain.APIException
import ai.ftech.ioesdk.publish.FTechIOEManager
import android.util.Log
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap

object RetrofitFactory {
    private val TAG = RetrofitFactory::class.java.simpleName
    private const val FIOE = "FIOE"
    private const val INIT_SDK = "INIT_SDK"

    private val builderMap = ConcurrentHashMap<String, RetrofitBuilderInfo>()

    fun <T> createFIOEService(service: Class<T>): T {
        synchronized(RetrofitBuilderInfo::class.java) {
            val builderInfo = RetrofitBuilderInfo()

            builderInfo.builder = IOERetrofitConfig().getRetrofitBuilder()

            builderMap[FIOE] = builderInfo
            Log.d(TAG, "Create new domain retrofit builder for ${ApiConfig.BASE_URL_IOE}")
            Log.e(TAG, "Reuse domain retrofit builder for ${ApiConfig.BASE_URL_IOE}")
            val serviceApi = builderInfo.builder?.build()?.create(service)

            return serviceApi ?: throw APIException(APIException.CREATE_INSTANCE_SERVICE_ERROR)
        }
    }

    fun <T> createInitSDKService(service: Class<T>): T {
        synchronized(RetrofitBuilderInfo::class.java) {
            val builderInfo = RetrofitBuilderInfo().apply {
            }
            builderInfo.builder = SDKInitRetrofitConfig().getRetrofitBuilder()
            builderMap[INIT_SDK] = builderInfo
            val serviceApi = builderInfo.builder?.build()?.create(service)
            return serviceApi ?: throw APIException(APIException.CREATE_INSTANCE_SERVICE_ERROR)
        }
    }

    class RetrofitBuilderInfo {
        var builder: Retrofit.Builder? = null
    }
}
