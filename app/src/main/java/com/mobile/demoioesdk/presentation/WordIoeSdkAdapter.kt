package com.mobile.demoioesdk.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobile.demoioesdk.R
import com.mobile.demoioesdk.domain.model.Word

class WordIoeSdkAdapter : RecyclerView.Adapter<WordIoeSdkAdapter.WordIoeSdkVH>() {

    var lWord: MutableList<Word> = mutableListOf()

    fun addList(newList: MutableList<Word>) {
        lWord.clear()
        lWord.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordIoeSdkVH {
        val fTabsView = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return WordIoeSdkVH(fTabsView)
    }

    override fun onBindViewHolder(holder: WordIoeSdkVH, position: Int) {
        holder.onBind(lWord[position])
    }

    override fun getItemCount(): Int {
        return lWord.size
    }

    inner class WordIoeSdkVH(itemView: View) : ViewHolder(itemView) {
        private var tvWordScore: AppCompatTextView
        private var tvWord: AppCompatTextView

        init {
            tvWordScore = itemView.findViewById(R.id.tvWordScore)
            tvWord = itemView.findViewById(R.id.tvWord)
        }

        fun onBind(word: Word) {
            tvWordScore.text = word.score.toString()
            tvWord.text = word.text
        }
    }
}
