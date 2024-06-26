package ai.ftech.ioesdk.base.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

abstract class BaseAction<REQUEST : BaseAction.RequestValue, RESPONSE> {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    protected abstract suspend fun execute(rv: REQUEST): RESPONSE

    suspend operator fun invoke(
        requestValue: REQUEST,
    ): Flow<RESPONSE> {
        val flow = flow {
            val response = withContext(dispatcher) {
                execute(requestValue)
            }
            emit(response)
        }.flowOn(dispatcher)
        return flow
    }

    class VoidRequest : RequestValue

    interface RequestValue

    interface IActionCallback<RESPONSE> {
        fun onSuccess(response: RESPONSE? = null)
        fun onException(throwable: Throwable?)
    }
}
