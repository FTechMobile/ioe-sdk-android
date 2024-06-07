package ai.ftech.ioesdk.domain.action

import ai.ftech.ioesdk.base.common.BaseAction
import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse
import ai.ftech.ioesdk.di.RepositoryFactory
import ai.ftech.ioesdk.domain.model.StopRecord

class StopRecordAction : BaseAction<StopRecordAction.StopRecordRV, StopRecordIOEResponse?>() {

    override suspend fun execute(rv: StopRecordRV): StopRecordIOEResponse? {
        val repo = RepositoryFactory.getIOE()
        return repo.stopRecord(rv.file, rv.requestId)
    }

    class StopRecordRV(var file: String, var requestId: String) : RequestValue
}
