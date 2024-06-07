package ai.ftech.ioesdk.common.message

import ai.ftech.ioesdk.domain.APIException
import android.content.Context

interface IAPIMessage {
    fun getAPIMessage(context: Context?, exception: APIException): String?
}
