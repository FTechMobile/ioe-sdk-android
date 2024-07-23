package ai.ftech.ioesdk.data.source.remote.network

object ApiConfig {
    const val BASE_URL_IOE = "https://fcloud-streaming-socket-api.ftech.ai"
    const val BASE_URL_INIT_IOE = "https://fcloud-api-gateway.ftech.ai"

    object HeaderName {
        const val CONTENT_TYPE = "Content-Type"
        const val AUTHORIZATION = "Authorization"
    }

    object HeaderValue {
        const val APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
        const val APPLICATION_JSON = "application/json"
    }

    enum class API_LANGUAGE(val language: String) {
        VI("vi")
    }
}
