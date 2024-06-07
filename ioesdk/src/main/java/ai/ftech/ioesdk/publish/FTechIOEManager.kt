package ai.ftech.ioesdk.publish

import ai.ftech.ioesdk.R
import ai.ftech.ioesdk.base.common.BaseAction
import ai.ftech.ioesdk.base.extension.setApplication
import ai.ftech.ioesdk.common.getAppString
import ai.ftech.ioesdk.common.onException
import ai.ftech.ioesdk.data.model.stoprecord.StopRecordIOEResponse
import ai.ftech.ioesdk.domain.APIException
import ai.ftech.ioesdk.domain.action.InitRecordAction
import ai.ftech.ioesdk.domain.action.StartRecordAction
import ai.ftech.ioesdk.domain.action.StopRecordAction
import ai.ftech.ioesdk.domain.model.InitRecord
import ai.ftech.ioesdk.domain.model.StartRecord
import ai.ftech.ioesdk.presentation.AppPreferences
import ai.ftech.ioesdk.speech.IAudioRecorder
import ai.ftech.ioesdk.speech.IOEAudioRecorder
import ai.ftech.ioesdk.utils.FileUtils
import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


object FTechIOEManager {
    private var applicationContext: Context? = null
    private var pendingCallback: (() -> Unit)? = null
    private var isActive = true
    private var ioeAudioRecord: IAudioRecorder? = null
    private var mRecordingCallback: IFTechRecordingCallback? = null

    @JvmStatic
    fun init(context: Context) {
        applicationContext = context
        setApplication(getApplicationContext())
        AppPreferences.init(context)
        ioeAudioRecord = IOEAudioRecorder(context)
    }

    @JvmStatic
    fun getApplicationContext(): Application {
        return applicationContext as? Application
            ?: throw RuntimeException("applicationContext must not null")
    }

    private fun <T> invokeCallback(callback: IFTechIOECallback<T>?, result: FTechIOEResult<T>) {
        when (result.type) {
            FTECH_IOE_RESULT_TYPE.SUCCESS -> {
                if (isActive) {
                    callback?.onSuccess(result.data!!)
                } else {
                    pendingCallback = {
                        callback?.onSuccess(result.data!!)
                    }
                }
            }

            FTECH_IOE_RESULT_TYPE.ERROR -> {
                if (isActive) {
                    callback?.onFail(result.error)
                } else {
                    pendingCallback = {
                        callback?.onFail(result.error)
                    }
                }
            }

            FTECH_IOE_RESULT_TYPE.CANCEL -> {
                if (isActive) {
                    callback?.onCancel()
                } else {
                    pendingCallback = {
                        callback?.onCancel()
                    }
                }
            }
        }
    }

    private fun <I : BaseAction.RequestValue, O> runActionInCoroutine(
        action: BaseAction<I, O>,
        request: I,
        callback: IFTechIOECallback<O>?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            action.invoke(request).onException {
                CoroutineScope(Dispatchers.Main).launch {
                    invokeCallback(callback, FTechIOEResult<O>().apply {
                        this.type = FTECH_IOE_RESULT_TYPE.ERROR
                        this.error = if (it is APIException) it else APIException(
                            APIException.UNKNOWN_ERROR,
                            it.message
                        )
                    })
                }
            }.collect {
                CoroutineScope(Dispatchers.Main).launch {
                    invokeCallback(callback, FTechIOEResult<O>().apply {
                        this.type = FTECH_IOE_RESULT_TYPE.SUCCESS
                        this.data = it
                    })
                }
            }
        }
    }

    @JvmStatic
    fun initRecord(
        appId: String,
        secretKey: String,
        callback: IFTechIOECallback<Boolean>
    ) {

        if (appId.isEmpty()) {
            callback.onFail(
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.empty_app_id)
                )
            )
            return
        }
        if (secretKey.isEmpty()) {
            callback.onFail(
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.empty_secret_key)
                )
            )
            return
        }

        runActionInCoroutine(
            InitRecordAction(),
            InitRecordAction.InitRecordActionRV(appId, secretKey),
            callback = object : IFTechIOECallback<Boolean> {
                override fun onSuccess(info: Boolean?) {
                    callback.onSuccess(info)
                }

                override fun onCancel() {
                    callback.onCancel()
                }

                override fun onFail(error: APIException?) {
                    callback.onFail(APIException(getAppString(R.string.ioe_init_not_success)))
                }
            })
    }

    @JvmStatic
    fun registerRecordingListener(recordingCallback: IFTechRecordingCallback) {
        mRecordingCallback = recordingCallback
    }

    @JvmStatic
    fun startRecord(
        referenceText: String,
        languageAccent: StartRecordAction.LanguageAccent,
        extraData: String?
    ) {

        if (!checkRecordAudioPermission()) {
            mRecordingCallback?.onFail(APIException(getAppString(R.string.message_permission_audio_denied)))
            return
        }

        if (ioeAudioRecord == null) {
            mRecordingCallback?.onFail(APIException(getAppString(R.string.message_sdk_not_initial)))
            return
        }

        if (AppPreferences.token.isNullOrEmpty()) {
            mRecordingCallback?.onFail(APIException(getAppString(R.string.message_recorder_not_initial)))
            return
        }

        if (referenceText.isEmpty()) {
            mRecordingCallback?.onFail(
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.empty_reference_text)
                )
            )
            return
        }

        ioeAudioRecord!!.registerRecordingListener(object : IAudioRecorder.IRecordingListener {
            override fun onStart() {
                mRecordingCallback?.onStart()
            }

            override fun onRecording() {
                mRecordingCallback?.onRecording()
            }

            override fun onComplete(fileRecord: File) {
                executeEvaluatePronunciation(fileRecord.absolutePath)
            }

            override fun onFail(reason: String) {
                AppPreferences.requestId = null
                mRecordingCallback?.onFail(APIException(reason))
            }
        })

        runActionInCoroutine(StartRecordAction(),
            StartRecordAction.StartRecordRV(referenceText, languageAccent, extraData),
            callback = object : IFTechIOECallback<StartRecord> {

                override fun onSuccess(info: StartRecord?) {
                    AppPreferences.requestId = info?.data?.requestId
                    ioeAudioRecord?.start()
                }

                override fun onFail(error: APIException?) {
                    mRecordingCallback?.onFail(APIException(error?.message))
                }
            })
    }

    @JvmStatic
    fun stopRecord() {
        if (ioeAudioRecord == null) {
            mRecordingCallback?.onFail(APIException(getAppString(R.string.message_sdk_not_initial)))
            return
        }

        if (AppPreferences.token.isNullOrEmpty()) {
            mRecordingCallback?.onFail(APIException(getAppString(R.string.message_recorder_not_initial)))
            return
        }

        val requestId = AppPreferences.requestId
        if (requestId.isNullOrEmpty()) {
            mRecordingCallback?.onFail(
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.empty_request_id)
                )
            )
            return
        }

        ioeAudioRecord!!.stop()
    }

    private fun executeEvaluatePronunciation(fileRecordPath: String) {
        val requestId = AppPreferences.requestId
        runActionInCoroutine(
            StopRecordAction(),
            StopRecordAction.StopRecordRV(fileRecordPath, requestId.toString()),
            callback = object : IFTechIOECallback<StopRecordIOEResponse?> {

                override fun onSuccess(info: StopRecordIOEResponse?) {
                    AppPreferences.requestId = null
                    FileUtils.deleteFile(fileRecordPath)
                    if (info != null) {
                        mRecordingCallback?.onComplete(info)
                    } else {
                        mRecordingCallback?.onFail(
                            APIException(
                                APIException.UNKNOWN_ERROR,
                                message = getAppString(R.string.message_error_evaluate_process)
                            )
                        )
                    }
                }

                override fun onFail(error: APIException?) {
                    AppPreferences.requestId = null
                    mRecordingCallback?.onFail(APIException(error?.message))
                }
            })
    }

    private fun checkRecordAudioPermission(): Boolean = ActivityCompat.checkSelfPermission(
        getApplicationContext(), Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
}
