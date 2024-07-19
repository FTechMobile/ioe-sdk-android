package ai.ftech.ioesdk.domain.action

import ai.ftech.ioesdk.base.common.BaseAction
import ai.ftech.ioesdk.di.RepositoryFactory
import ai.ftech.ioesdk.domain.model.StartRecord

class StartRecordAction : BaseAction<StartRecordAction.StartRecordRV, StartRecord>() {
    override suspend fun execute(rv: StartRecordRV): StartRecord {
        val repo = RepositoryFactory.getIOE()
        return repo.startRecord(rv.referenceText, rv.languageAccent.value, rv.extraData)
    }

    class StartRecordRV(var referenceText: String, var languageAccent: LanguageAccent, var extraData: String?) : RequestValue

    enum class LanguageAccent(val value : String){
        EN_US("en-gb")
    }
}
