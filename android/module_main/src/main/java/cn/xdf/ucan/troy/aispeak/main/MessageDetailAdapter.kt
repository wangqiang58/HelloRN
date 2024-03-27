package cn.xdf.ucan.troy.aispeak.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.xdf.ucan.troy.aispeak.Constant
import cn.xdf.ucan.troy.aispeak.bean.NextCommandResponseBean
import com.wx.ovalimageview.RoundImageView

class MessageDetailAdapter(
    private val context: ChatActivity
) : RecyclerView.Adapter<MessageDetailAdapter.ViewHolder>() {
    public var commandListener: CommandListener? = null

    interface CommandListener {
        fun onItemCommand(v: View, position: Int, items: MutableList<NextCommandResponseBean>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = when (viewType) {
            1 -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_msg_list_left, parent, false)

            2 -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_msg_list_right, parent, false)

            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        if (context.mList?.get(position)?.actionRole.equals(Constant.TEACHER)) {
            return 1
        }
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var messageBean = context.mList!!.get(position)
        holder.mItemMsg.text = messageBean!!.commandContentDetail!!.text
        if (messageBean!!.actionRole.equals(Constant.TEACHER) && position == itemCount-1){//只播放最后加进来的内容
            commandListener?.onItemCommand(holder.itemView, position, context.mList!!)
        }

//        holder.itemView.setOnClickListener{
//            commandListener?.onItemCommand(holder.itemView, position, context.mList!!)
//        }
    }

    override fun getItemCount(): Int {
        return context.mList?.size!!
    }

    /**
     * ViewHolder
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mItemMsg: TextView = itemView.findViewById(R.id.item_msg)
        val mHeadImg: RoundImageView = itemView.findViewById(R.id.head_img)
    }
}