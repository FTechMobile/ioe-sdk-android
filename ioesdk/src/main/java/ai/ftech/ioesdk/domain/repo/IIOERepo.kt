package ai.ftech.ioesdk.domain.repo

import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse
import ai.ftech.ioesdk.domain.model.StopRecord
import ai.ftech.ioesdk.domain.model.InitRecord
import ai.ftech.ioesdk.domain.model.StartRecord

interface IIOERepo {
    fun initRecord(appId: String, secretKey: String): Boolean

    fun startRecord(referenceText: String, englishAccent: String, extraData: String?): StartRecord

    fun stopRecord(file: String, requestId: String): StopRecordIOEResponse?
}
