package ai.ftech.ioesdk.domain

internal class DBException : ActionException {

    constructor() : super()

    constructor(message: String?) : super(message) {}

    constructor(t: Throwable?) : super(t) {}

    constructor(message: String?, t: Throwable?) : super(message, t) {}
}
