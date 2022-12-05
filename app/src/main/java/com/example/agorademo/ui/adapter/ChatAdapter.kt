package com.example.agorademo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.agorademo.R
import com.example.agorademo.databinding.AdaperChatCurrentUserBinding
import com.example.agorademo.databinding.AdaperChatOtherUserBinding
import com.example.agorademo.databinding.AdapterChatListBinding
import com.example.agorademo.model.ChatData
import java.lang.RuntimeException


class ChatAdapter(val chatList: ArrayList<ChatData>, val currentUserId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_CURRENT_USER = 1
    private val TYPE_OTHER_USER = 2

    inner class CurrentUserHolder(val binding: AdaperChatCurrentUserBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }

        fun updateView(position: Int) {
            val chatData = chatList[adapterPosition]
            binding.tvSenderName.text = chatData.senderName
            binding.tvCurrentUserMessag.text = chatData.chatName
        }
    }

    inner class OtherUserHolder(val binding: AdaperChatOtherUserBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }

        fun updateView(position: Int) {
            val chatData = chatList[adapterPosition]
            binding.tvSenderName.text = chatData.senderName
            binding.tvOtherUser.text = chatData.chatName
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==TYPE_CURRENT_USER){
            val binding: AdaperChatCurrentUserBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.adaper_chat_current_user, parent, false
            )
            return CurrentUserHolder(binding)
        }else if (viewType==TYPE_OTHER_USER){
            val binding: AdaperChatOtherUserBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.adaper_chat_other_user, parent, false
            )
            return OtherUserHolder(binding)
        }else{
            throw RuntimeException("Unknown User type")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CurrentUserHolder){
            holder.updateView(position)
        } else if (holder is OtherUserHolder){
            holder.updateView(position)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].senderId == currentUserId) TYPE_CURRENT_USER else TYPE_OTHER_USER
    }
}