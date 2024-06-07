package ai.ftech.ioesdk.domain

class APIException : Exception {
    companion object {
        //sdk error code
        const val UNKNOWN_ERROR = -1
        const val NETWORK_ERROR = -2
        const val TIME_OUT_ERROR = -3
        const val SERVER_ERROR_CODE_UNDEFINE = -4
        const val RESPONSE_BODY_ERROR = -5
        const val CREATE_INSTANCE_SERVICE_ERROR = -6

        //server error code
        const val EXPIRE_SESSION_ERROR = 4030
        const val LIMIT_SESSION_ERROR = 4027

        //start record
        //!200
        const val DONT_CONNECT_TO_SERVER = -100

        //200
        const val SUCCESS = 0
        const val INTERNAL_SERVER_ERROR = 5000
        const val DONT_RECOGNIZE_PRONUNCIATION = 4007
        const val BAD_REQUEST = 4000
        const val CANT_INIT_SDK = -9

    }

    var code = 0
        private set
    var payload: Any? = null
        private set

    constructor(message: String?) : super(message) {}

    constructor(t: Throwable?) : super(t) {}

    constructor(message: String?, t: Throwable?) : super(message, t) {}

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(code: Int, message: String?) : super(message) {
        this.code = code
    }

    constructor(code: Int, t: Throwable?) : super(t) {
        this.code = code
    }

    constructor(code: Int, message: String?, payload: Any?) : super(message) {
        this.code = code
        this.payload = payload
    }
}
