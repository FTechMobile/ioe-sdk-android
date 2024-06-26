package ai.ftech.ioesdk.base.common

/**
 * Sử dụng để map tự động các field model trong content resolver
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ContentProviderInfo(val getFieldName: String)
