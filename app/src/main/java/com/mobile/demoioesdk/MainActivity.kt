package com.mobile.demoioesdk

import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse
import ai.ftech.ioesdk.domain.APIException
import ai.ftech.ioesdk.domain.action.StartRecordAction
import ai.ftech.ioesdk.publish.FTechIOEManager
import ai.ftech.ioesdk.publish.IFTechIOECallback
import ai.ftech.ioesdk.publish.IFTechRecordingCallback
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mobile.demoioesdk.domain.model.Alphabet
import com.mobile.demoioesdk.domain.model.Word
import com.mobile.demoioesdk.presentation.AlphabetIoeSdkAdapter
import com.mobile.demoioesdk.presentation.PlayerUtils
import com.mobile.demoioesdk.presentation.PronounceDialog
import com.mobile.demoioesdk.presentation.WordIoeSdkAdapter

class MainActivity : AppCompatActivity() {

    private var wordIoeSdkAdapter: WordIoeSdkAdapter? = null
    private var alphabetIoeSdkAdapter: AlphabetIoeSdkAdapter? = null
    private lateinit var btnInit: AppCompatButton
    private lateinit var btnStart: AppCompatButton
    private lateinit var btnStop: AppCompatButton
    private lateinit var btnPlayAudio: AppCompatButton
    private lateinit var tvStart: AppCompatTextView
    private lateinit var tvResultPronunciation: AppCompatTextView
    private lateinit var rvMainWord: RecyclerView
    private lateinit var rvMainAlphabet: RecyclerView
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted, you can start recording audio
            } else {
                // Permission denied, handle accordingly
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)
        btnInit = findViewById(R.id.btnInit)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        btnPlayAudio = findViewById(R.id.btnMainPlayAudio)
        tvStart = findViewById(R.id.tvStart)
        rvMainWord = findViewById(R.id.rvMainWord)
        rvMainAlphabet = findViewById(R.id.rvMainAlphabet)
        tvResultPronunciation = findViewById(R.id.tvResultPronunciation)
        FTechIOEManager.init(applicationContext)

        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, you can start recording audio
        } else {
            // Request the permission
            requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        }

        FTechIOEManager.registerRecordingListener(object : IFTechRecordingCallback {
            override fun onStart() {
                tvStart.text = "Recording..."
                tvResultPronunciation.text = ""
                tvResultPronunciation.setOnClickListener(null)
            }

            override fun onRecording() {
            }

            override fun onFail(error: APIException) {
                tvStart.text = ""
                tvResultPronunciation.text = ""
                tvResultPronunciation.setOnClickListener(null)
                Toast.makeText(this@MainActivity, error.message
                    ?: "error", Toast.LENGTH_SHORT).show()
            }

            override fun onComplete(result: StopRecordIOEResponse) {
                tvStart.text = ""
                tvResultPronunciation.text = result.data?.minioLink
                btnPlayAudio.setOnClickListener {
                    if (result.data?.minioLink != null) {
                        PlayerUtils.playAudio(result.data?.minioLink!!)
                    }
                }
                getList(result)
                tvResultPronunciation.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(result.data?.minioLink)))
                }
            }
        })

        btnInit.setOnClickListener {
            FTechIOEManager.initRecord("100067", "0341311b733b965218182220a507a3bc", object : IFTechIOECallback<Boolean> {
                override fun onSuccess(info: Boolean?) {
                    tvStart.text = "Init success, Start Record"
                    tvStart.setOnClickListener(null)
                }

                override fun onCancel() {
                    Toast.makeText(this@MainActivity, "cancel", Toast.LENGTH_SHORT).show()
                }

                override fun onFail(error: APIException?) {
                    Toast.makeText(this@MainActivity, error?.message
                        ?: "error", Toast.LENGTH_SHORT).show()
                    tvStart.text = "Hello world"
                    tvStart.setOnClickListener(null)
                }
            })
        }

        btnStart.setOnClickListener {
            FTechIOEManager.startRecord("recording", StartRecordAction.LanguageAccent.EN_US, null)
        }

        btnStop.setOnClickListener {
            FTechIOEManager.stopRecord()
        }
    }

    fun getList(result: StopRecordIOEResponse) {
        val lW = mutableListOf<Word>()
        val lAlphabet = mutableListOf<Alphabet>()
        val lAll = result.data?.scoreData?.wordsScoreDetail

        lAll?.forEachIndexed { index, wordsScoreDetail ->
            wordsScoreDetail.apply {
                !word.isNullOrEmpty() && accuracyScore != null && lW.add(Word(index, word!!, accuracyScore!!))
            }

            wordsScoreDetail.phonemesScoreDetail?.forEachIndexed { index, phonemesScoreDetail ->
                phonemesScoreDetail.apply {
                    !phoneme.isNullOrEmpty() && accuracyScore != null && lAlphabet.add(Alphabet(phoneme!!, accuracyScore!!, audioLink, description))
                }
            }
        }

        setUpAdapterWord(lW)
        setUpAdapterAlphabet(lAlphabet)
    }

    fun setUpAdapterWord(lW: MutableList<Word>) {
        wordIoeSdkAdapter = WordIoeSdkAdapter()
        wordIoeSdkAdapter?.addList(lW)
        rvMainWord.adapter = wordIoeSdkAdapter
        val layoutManager = FlexboxLayoutManager(baseContext).apply {
            justifyContent = JustifyContent.CENTER
            flexDirection = FlexDirection.ROW
        }
        rvMainWord.layoutManager = layoutManager
    }

    fun setUpAdapterAlphabet(lAlphabet: MutableList<Alphabet>) {
        alphabetIoeSdkAdapter = AlphabetIoeSdkAdapter()
        alphabetIoeSdkAdapter?.addList(lAlphabet)
        rvMainAlphabet.adapter = alphabetIoeSdkAdapter
        val layoutManager = FlexboxLayoutManager(baseContext).apply {
            justifyContent = JustifyContent.CENTER
            flexDirection = FlexDirection.ROW
        }
        rvMainAlphabet.layoutManager = layoutManager

        alphabetIoeSdkAdapter?.listener = object : AlphabetIoeSdkAdapter.IAlphabetListener {
            override fun onClickItem(item: Alphabet) {
                showDialog(item)
            }
        }
    }

    fun showDialog(item: Alphabet) {
        PronounceDialog.Builder().setAlphabet(item).show(supportFragmentManager)
    }
}
