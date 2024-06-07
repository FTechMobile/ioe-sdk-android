package ai.ftech.ioesdk.data.converter

import ai.ftech.ioesdk.base.common.converter.Converter
import ai.ftech.ioesdk.base.common.converter.IConverter
import ai.ftech.ioesdk.base.common.converter.Mapper
import ai.ftech.ioesdk.data.model.initrecord.InitRecordIOEResponse
import ai.ftech.ioesdk.domain.model.InitRecord

class InitRecordIOEResponseConvertToInitRecord : IConverter<InitRecordIOEResponse?, InitRecord> {
    override fun convert(source: InitRecordIOEResponse?): InitRecord {
        if (source == null) return InitRecord()
        val mapper = Mapper(InitRecordIOEResponse::class, InitRecord::class).apply {
            register(
                InitRecord::data,
                Mapper(InitRecordIOEResponse.InitRecordIOEData::class, InitRecord.InitRecordIOEData::class).apply {
                    register(
                        InitRecord.InitRecordIOEData::service,
                        Mapper(InitRecordIOEResponse.InitRecordIOEData.Service::class, InitRecord.InitRecordIOEData.Service::class)
                    )
                }
            )
        }
        return mapper.invoke(source)
    }
}
