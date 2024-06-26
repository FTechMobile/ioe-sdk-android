package ai.ftech.ioesdk.base.cache

interface ICacheFactory<T> {
    fun createCache(name: String?, maxSize: Int): ICache<String?, T>?
}
