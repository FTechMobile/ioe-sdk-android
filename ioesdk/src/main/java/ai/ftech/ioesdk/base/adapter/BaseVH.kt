package ai.ftech.ioesdk.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseVH<DATA>(view: View) : RecyclerView.ViewHolder(view) {
    open fun onBind(data: DATA) {}
    open fun onBind(data: DATA, payloads: List<Any>) {}
}
