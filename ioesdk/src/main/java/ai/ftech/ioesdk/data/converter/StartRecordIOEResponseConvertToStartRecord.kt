package ai.ftech.ioesdk.data.converter

import ai.ftech.ioesdk.base.common.converter.IConverter
import ai.ftech.ioesdk.base.common.converter.Mapper
import ai.ftech.ioesdk.data.model.startrecord.StartRecordIOEResponse
import ai.ftech.ioesdk.domain.model.StartRecord

class StartRecordIOEResponseConvertToStartRecord : IConverter<StartRecordIOEResponse?, StartRecord> {
    override fun convert(source: StartRecordIOEResponse?): StartRecord {
        if (source == null) return StartRecord()
        val mapper = Mapper(StartRecordIOEResponse::class, StartRecord::class).apply {
            register(
                StartRecord::data,
                Mapper(StartRecordIOEResponse.StartRecordIOEData::class, StartRecord.StartRecordIOEData::class))
        }
        return mapper.invoke(source)
    }
}
