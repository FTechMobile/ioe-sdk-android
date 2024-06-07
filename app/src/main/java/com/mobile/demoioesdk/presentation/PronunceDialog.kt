package com.mobile.demoioesdk.presentation

import ai.ftech.ioesdk.base.common.BaseDialog
import ai.ftech.ioesdk.base.common.DialogScreen
import android.text.Html
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import com.mobile.demoioesdk.R
import com.mobile.demoioesdk.domain.model.Alphabet

class PronounceDialog(
    private var alphabet: Alphabet = Alphabet()
) : BaseDialog(R.layout.pronounce_layout) {

    private lateinit var ivPronounceCloseDialog: AppCompatImageView
    private lateinit var ivPronouncePlay: AppCompatImageView
    private lateinit var tvPronounceAlphabet: AppCompatTextView
    private lateinit var tvPronounceScore: AppCompatTextView
    private lateinit var tvPronounceDescription: AppCompatTextView
    private lateinit var btnPronounceClose: AppCompatButton

    override fun onInitView() {
        ivPronounceCloseDialog = viewRoot.findViewById(R.id.ivPronounceCloseDialog)
        ivPronouncePlay = viewRoot.findViewById(R.id.ivPronouncePlay)
        tvPronounceAlphabet = viewRoot.findViewById(R.id.tvPronounceAlphabet)
        tvPronounceScore = viewRoot.findViewById(R.id.tvPronounceScore)
        tvPronounceDescription = viewRoot.findViewById(R.id.tvPronounceDescription)
        btnPronounceClose = viewRoot.findViewById(R.id.btnPronounceClose)

        tvPronounceAlphabet.text = alphabet.text
        tvPronounceDescription.text = Html.fromHtml(alphabet.description, Html.FROM_HTML_MODE_COMPACT)
        tvPronounceScore.text = alphabet.score.toString()
        ivPronouncePlay.setOnClickListener {
            alphabet.audioAlphabet?.let { PlayerUtils.playAudio(it) }
        }

        btnPronounceClose.setOnClickListener {
            dismissDialog()
        }

        ivPronounceCloseDialog.setOnClickListener {
            dismissDialog()
        }
    }

    override fun getBackgroundId(): Int = R.id.constPronounce

    override fun screen(): DialogScreen {
        return DialogScreen().apply {
            mode = DialogScreen.DIALOG_MODE.NORMAL
            isFullHeight = false
            isFullWidth = false
        }
    }

    class Builder {
        private var alphabet: Alphabet = Alphabet()

        fun setAlphabet(alphabet: Alphabet) = apply {
            this.alphabet = alphabet
        }

        fun show(fragmentManager: FragmentManager?) {
            if (fragmentManager == null) return
            return PronounceDialog(alphabet).show(fragmentManager, null)
        }
    }
}
