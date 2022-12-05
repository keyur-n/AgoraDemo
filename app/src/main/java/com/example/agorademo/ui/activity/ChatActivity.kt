package com.example.agorademo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agorademo.R
import com.example.agorademo.databinding.ActivityChatBinding
import com.example.agorademo.initAgoraSDK
import com.example.agorademo.model.ChatData
import com.example.agorademo.model.ChatListData
import com.example.agorademo.model.getCurrentUser
import com.example.agorademo.ui.adapter.ChatAdapter
import com.example.agorademo.utils.PARAM_RECEIVER_ID
import io.agora.CallBack
import io.agora.ConnectionListener
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import io.agora.chat.TextMessageBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var receiverId: String
    private lateinit var currentUserId: String
    private var list = ArrayList<ChatData>()
    private val currentUser by lazy { getCurrentUser(true) }
    private val mAdapter by lazy { ChatAdapter(list, currentUserId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        initAgoraSDK()
        initUI()
        initListener()
        initClickListener()
        initData()
    }

    private fun initData() {
        receiverId = intent.getStringExtra(PARAM_RECEIVER_ID) ?: return finish()
    }


    private fun initUI() {
        currentUserId=currentUser.username.lowercase()

        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun initListener() {
        // Adds message event callbacks.
        ChatClient.getInstance().chatManager().addMessageListener { messages: List<ChatMessage> ->
            for (message in messages) {
                val builder = StringBuilder()
                builder.append("Receive a ").append(message.type.name)
                    .append(" message from: ").append(message.from)
                if (message.type == ChatMessage.Type.TXT) {
                    builder.append(" content:")
                        .append((message.body as TextMessageBody).message)
                }
                val chatData = ChatData(
                    chatId = message.msgId,
                    chatName = (message.body as TextMessageBody).message,
                    senderId = message.from,
                    senderName = message.userName
                )

                insertItem(chatData)
                showLog(builder.toString(), false)
            }
        }
        // Adds connection event callbacks.
        ChatClient.getInstance().addConnectionListener(object : ConnectionListener {
            override fun onConnected() {
                showLog("onConnected", false)
            }

            override fun onDisconnected(error: Int) {
                showLog("onDisconnected: $error", false)
            }

            override fun onLogout(errorCode: Int) {
                showLog("User needs to log out: $errorCode", false)
                ChatClient.getInstance().logout(false, null)
            }

            // This callback occurs when the token expires. When the callback is triggered, the app client must get a new token from the app server and logs in to the app again.
            override fun onTokenExpired() {
                showLog("ConnectionListener onTokenExpired", true)
            }

            // This callback occurs when the token is about to expire.
            override fun onTokenWillExpire() {
                showLog("ConnectionListener onTokenWillExpire", true)
            }
        })
    }

    private fun insertItem(chatData: ChatData) {
        lifecycleScope.launch(Dispatchers.Main){
            list.add(chatData)
            mAdapter.notifyDataSetChanged()
        }

    }

    private fun initClickListener() {
        binding.ibSend.setOnClickListener {
            val chatMessage = binding.etMsgContent.text.toString().trim()
            if (chatMessage.isEmpty()) {
                return@setOnClickListener
            }
            sendFirstMessage(chatMessage)
            binding.etMsgContent.setText("")
        }
    }

    // Shows logs.
    private fun showLog(content: String, showToast: Boolean) {
        if (TextUtils.isEmpty(content)) {
            return
        }
        runOnUiThread {
            if (showToast) {
                Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Sends the first message.
    fun sendFirstMessage(content: String) {

        // Creates a text message.
        val message = ChatMessage.createTextSendMessage(content, receiverId)
        message.setChatType(ChatMessage.ChatType.GroupChat);

        // Sets the message callback before sending the message.
        message.setMessageStatusCallback(object : CallBack {
            override fun onSuccess() {
                showLog("Send message success!", true)
                val chatData=ChatData(
                    chatId = message.msgId,
                    chatName = (message.body as TextMessageBody).message,
                    senderId = message.from,
                    senderName = message.userName
                )
                insertItem(chatData)
            }

            override fun onError(code: Int, error: String) {
                showLog(error, true)
                if (code == 200) {

                }
            }
        })

        // Sends the message.
        ChatClient.getInstance().chatManager().sendMessage(message)
    }

    companion object {

        fun newIntent(context: Context, chatData: ChatListData) {

            context.startActivity(Intent(context, ChatActivity::class.java).apply {
                putExtra(PARAM_RECEIVER_ID,chatData.chatId)
            })
        }
    }
}