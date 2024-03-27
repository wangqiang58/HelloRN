package cn.xdf.ucan.troy.aispeak.main


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.xdf.ucan.troy.aispeak.main.databinding.ItemTopicBinding

class TopicMessageAdapter(val context: TopicActivity) :
    RecyclerView.Adapter<TopicMessageAdapter.ViewHolder>() {
    private lateinit var mItemTopicView: ItemTopicBinding
    public var mClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun itemClick(topicId: Long)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mItemTopicView = ItemTopicBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(mItemTopicView.root)
    }

    override fun getItemCount(): Int {
        if(context.mList.isNullOrEmpty()){
            return 0
        }
        return context.mList!!.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (context.mList.isNullOrEmpty()) return
        if(context.mList != null){
            context.mList!!.get(position).let {
                holder.mTopicNameView.text = it!!.name
            }
            holder.itemView.setOnClickListener {
                mClickListener!!.itemClick(context.mList.get(position)!!.id)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTopicNameView: TextView = itemView.findViewById(R.id.topic_name)
    }
}