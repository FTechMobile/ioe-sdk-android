package ai.ftech.ioesdk.base.common.converter

interface IConverter<S, D> {
    fun convert(source: S): D
}
