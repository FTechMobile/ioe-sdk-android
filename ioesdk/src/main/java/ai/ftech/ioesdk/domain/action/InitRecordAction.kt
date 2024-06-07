package ai.ftech.ioesdk.domain.action

import ai.ftech.ioesdk.base.common.BaseAction
import ai.ftech.ioesdk.di.RepositoryFactory
import ai.ftech.ioesdk.domain.model.InitRecord

class InitRecordAction : BaseAction<InitRecordAction.InitRecordActionRV, Boolean>() {
    override suspend fun execute(rv: InitRecordActionRV): Boolean {
        val repo = RepositoryFactory.getIOE()
        return repo.initRecord(rv.appId, rv.secretKey)
    }

    class InitRecordActionRV(var appId: String, var secretKey: String) : RequestValue
}
