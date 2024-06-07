package ai.ftech.ioesdk.data.converter

import ai.ftech.ioesdk.base.common.converter.IConverter
import ai.ftech.ioesdk.base.common.converter.Mapper
import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse
import ai.ftech.ioesdk.domain.model.StopRecord

class StopRecordResponseConvertToStopRecord : IConverter<StopRecordIOEResponse?, StopRecord> {
    override fun convert(source: StopRecordIOEResponse?): StopRecord {
        if (source == null) return StopRecord()
        val mapper = Mapper(StopRecordIOEResponse::class, StopRecord::class).apply {
            register(
                StopRecord::data,
                Mapper(
                    StopRecordIOEResponse.StopRecordData::class,
                    StopRecord.StopRecordData::class
                )
            )
        }
        return mapper.invoke(source)
    }
}
