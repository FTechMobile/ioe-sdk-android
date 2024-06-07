package ai.ftech.ioesdk.domain.model

class InitRecord {
    var data: InitRecordIOEData? = null
    var ts: String? = null

    class InitRecordIOEData {

        var token: String? = null
        var service: Service? = null

        class Service {

            var id: Int? = null

            var code: String? = null

            var name: String? = null

            var link: String? = null

            var image: String? = null

            var info: String? = null

            var status: Int? = null
        }
    }
}
