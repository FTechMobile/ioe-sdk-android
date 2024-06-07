package ai.ftech.ioesdk.domain.model

import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse

class StopRecord {

    val data: StopRecordData? = null

    class StopRecordData {

        val requestId: String? = null

        val scoreData: ScoreData? = null

        val minioLink: String? = null
    }

    class WordsScoreDetail {
        val word: String? = null

        val phonemeWord: String? = null

        val accuracyScore: Int? = null

        val phonemesScoreDetail: List<StopRecordIOEResponse.PhonemesScoreDetail>? = null
    }

    class ScoreData {

        val accuracyScore: Int? = null

        val processingTime: Double? = null

        val wordsPerMin: Int? = null

        val fluencyScore: String? = null

        val speakingDuration: Int? = null

        val userPhoneme: String? = null

        val wordsScoreDetail: List<WordsScoreDetail>? = null

        val phonemeScoreStatistics: List<PhonemeScoreStatistic>? = null

        val requestId: String? = null

        val kenlmLog: KenlmLog? = null
    }

    class PhonemesScoreDetail {
        val phoneme: String? = null
        val accuracyScore: Int? = null
        val audioLink: Int? = null
        val description: String? = null
    }

    class PhonemeScoreStatistic {
        val groupName: List<String>? = null
        val phones: List<String>? = null
        val score: List<Int>? = null
        val meanScore: String? = null
    }

    class KenlmLog {
        val requestId: String? = null
        val referenceText: String? = null
        val userPhoneme: String? = null
        val accuracyScore: Int? = null
        val latency: Double? = null
    }
}
