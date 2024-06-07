package com.mobile.demoioesdk.presentation

import ai.ftech.ioesdk.common.getAppColor
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mobile.demoioesdk.R
import com.mobile.demoioesdk.domain.model.Alphabet

class AlphabetIoeSdkAdapter : RecyclerView.Adapter<AlphabetIoeSdkAdapter.AlphabetIoeSdkVH>() {

    var lAlphabet: MutableList<Alphabet> = mutableListOf()
    var listener: IAlphabetListener? = null

    fun addList(newList: MutableList<Alphabet>) {
        lAlphabet.clear()
        lAlphabet.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphabetIoeSdkVH {
        val fTabsView = LayoutInflater.from(parent.context).inflate(R.layout.alphabet_item, parent, false)
        return AlphabetIoeSdkVH(fTabsView)
    }

    override fun onBindViewHolder(holder: AlphabetIoeSdkVH, position: Int) {
        holder.onBind(lAlphabet[position])
    }

    override fun getItemCount(): Int {
        return lAlphabet.size
    }

    inner class AlphabetIoeSdkVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvAlphabetScore: AppCompatTextView
        private var tvAlphabetWord: AppCompatTextView
        private var constAlphabetRoot: ConstraintLayout

        init {
            tvAlphabetScore = itemView.findViewById(R.id.tvAlphabetScore)
            tvAlphabetWord = itemView.findViewById(R.id.tvAlphabetWord)
            constAlphabetRoot = itemView.findViewById(R.id.constAlphabetRoot)
            constAlphabetRoot.setOnClickListener {
                listener?.onClickItem(lAlphabet[adapterPosition])
            }
        }

        fun onBind(alphabet: Alphabet) {
            tvAlphabetWord.text = setUnderlineWithColor(alphabet.text, getAppColor(R.color.blue))
            tvAlphabetScore.text = alphabet.score.toString()

        }

        fun setUnderlineWithColor(text: String, underlineColor: Int): SpannableString {
            val spannableString = SpannableString(text)
            spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
            spannableString.setSpan(ForegroundColorSpan(underlineColor), 0, spannableString.length, 0)
            return spannableString
        }
    }

    interface IAlphabetListener {
        fun onClickItem(item: Alphabet)
    }
}
