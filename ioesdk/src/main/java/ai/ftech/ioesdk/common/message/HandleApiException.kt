package ai.ftech.ioesdk.common.message

import ai.ftech.ioesdk.R
import ai.ftech.ioesdk.common.getAppString
import ai.ftech.ioesdk.domain.APIException
import android.content.Context

object HandleApiException : IAPIMessage {

    override fun getAPIMessage(context: Context?, exception: APIException): String {
        return when (exception.code) {
            APIException.NETWORK_ERROR -> getAppString(R.string.ioe_no_network)
            APIException.TIME_OUT_ERROR -> getAppString(R.string.ioe_sever_time_out)
            APIException.EXPIRE_SESSION_ERROR -> getAppString(R.string.ioe_session_expire)
            else -> {
                exception.message ?: getAppString(R.string.ioe_unknown_error)
            }
        }
    }
}
