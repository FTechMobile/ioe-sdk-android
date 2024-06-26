package ai.ftech.ioesdk.data.source.remote.network

import ai.ftech.ioesdk.presentation.AppPreferences
import com.google.android.gms.common.api.Api
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class IOERetrofitConfig : BaseRetrofitConfig() {
    override fun getUrl() = ApiConfig.BASE_URL_IOE

    override fun getInterceptorList(): Array<Interceptor> {
        return arrayOf(ContentTypeJson())
    }

    class ContentTypeJson() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = buildChain(chain)
            return chain.proceed(requestBuilder.build())
        }

        private fun buildChain(chain: Interceptor.Chain): Request.Builder {
            val original = chain.request()
            val builder = original.newBuilder()
            builder.addHeader(
                ApiConfig.HeaderName.CONTENT_TYPE,
                ApiConfig.HeaderValue.APPLICATION_JSON
            )
            if (!AppPreferences.token.isNullOrEmpty()) {
                builder.addHeader(
                    ApiConfig.HeaderName.AUTHORIZATION,
                    "Bearer ${AppPreferences.token}"
                )
            }
            builder.method(original.method, original.body)
            return builder
        }
    }
}
