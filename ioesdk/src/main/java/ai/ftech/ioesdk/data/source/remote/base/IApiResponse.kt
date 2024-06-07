package ai.ftech.ioesdk.data.source.remote.base

import ai.ftech.ioesdk.base.common.converter.IConverter

interface IApiResponse {
    fun isSuccessful(): Boolean

    fun <S, D> convert(source: S, converter: IConverter<S, D>): D? {
        return converter.convert(source)
    }
}
