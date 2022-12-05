package com.example.agorademo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.agorademo.R
import com.example.agorademo.databinding.AdapterChatListBinding
import com.example.agorademo.model.ChatListData
import com.example.agorademo.ui.activity.ChatActivity


class ChatListAdapter(val chatList:ArrayList<ChatListData>) : RecyclerView.Adapter<ChatListAdapter.DataObjectHolder>() {
    inner class DataObjectHolder(val binding: AdapterChatListBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val chatData=chatList[adapterPosition]
            v?.context?.let { ChatActivity.newIntent(it,chatData) }
        }

        fun updateView(position: Int) {
            val chatData=chatList[adapterPosition]
            binding.tvChatName.text=chatData.chatName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        val binding: AdapterChatListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.adapter_chat_list, parent, false
        )
        return DataObjectHolder(binding)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.updateView(position)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}