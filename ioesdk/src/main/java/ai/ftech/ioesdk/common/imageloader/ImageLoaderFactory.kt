package ai.ftech.ioesdk.common.imageloader

object ImageLoaderFactory {
    private val imageLoader = GlideImageLoaderImpl()

    fun glide(): IImageLoader {
        return imageLoader
    }
}
