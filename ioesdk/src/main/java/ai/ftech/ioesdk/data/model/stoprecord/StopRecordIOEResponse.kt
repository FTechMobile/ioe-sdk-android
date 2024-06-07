package ai.ftech.ioesdk.data.model.stoprecord

import ai.ftech.ioesdk.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StopRecordIOEResponse : BaseApiResponse() {

    @SerializedName("data")
    @Expose
    val data: StopRecordData? = null

    class StopRecordData {

        @SerializedName("request_id")
        @Expose
        val requestId: String? = null

        @SerializedName("score_data")
        @Expose
        val scoreData: ScoreData? = null

        @SerializedName("minio_link")
        @Expose
        val minioLink: String? = null
    }

    class WordsScoreDetail {
        @SerializedName("word")
        @Expose
        val word: String? = null

        @SerializedName("phoneme_word")
        @Expose
        val phonemeWord: String? = null

        @SerializedName("accuracy_score")
        @Expose
        val accuracyScore: Int? = null

        @SerializedName("phonemes_score_detail")
        @Expose
        val phonemesScoreDetail: List<PhonemesScoreDetail>? = null
    }

    class ScoreData {

        @SerializedName("accuracy_score")
        @Expose
        val accuracyScore: Int? = null

        @SerializedName("processing_time")
        @Expose
        val processingTime: Double? = null

        @SerializedName("words_per_min")
        @Expose
        val wordsPerMin: Int? = null

        @SerializedName("fluency_score")
        @Expose
        val fluencyScore: String? = null

        @SerializedName("speaking_duration")
        @Expose
        val speakingDuration: Int? = null

        @SerializedName("user_phoneme")
        @Expose
        val userPhoneme: String? = null

        @SerializedName("words_score_detail")
        @Expose
        val wordsScoreDetail: List<WordsScoreDetail>? = null

        @SerializedName("phoneme_score_statistics")
        @Expose
        val phonemeScoreStatistics: List<PhonemeScoreStatistic>? = null

        @SerializedName("request_id")
        @Expose
        val requestId: String? = null

        @SerializedName("kenlm_log")
        @Expose
        val kenlmLog: KenlmLog? = null
    }

    class PhonemesScoreDetail {

        @SerializedName("phoneme")
        @Expose
        val phoneme: String? = null

        @SerializedName("accuracy_score")
        @Expose
        val accuracyScore: Int? = null

        @SerializedName("audio_link")
        @Expose
        val audioLink: String? = null

        @SerializedName("description")
        @Expose
        val description: String? = null
    }

    class PhonemeScoreStatistic {

        @SerializedName("group_name")
        @Expose
        val groupName: List<String>? = null

        @SerializedName("phones")
        @Expose
        val phones: List<String>? = null

        @SerializedName("score")
        @Expose
        val score: List<Int>? = null

        @SerializedName("mean_score")
        @Expose
        val meanScore: String? = null
    }

    class KenlmLog {
        @SerializedName("request_id")
        @Expose
        val requestId: String? = null

        @SerializedName("reference_text")
        @Expose
        val referenceText: String? = null

        @SerializedName("user_phoneme")
        @Expose
        val userPhoneme: String? = null

        @SerializedName("accuracy_score")
        @Expose
        val accuracyScore: Int? = null

        @SerializedName("latency")
        @Expose
        val latency: Double? = null
    }

}
