package com.example.agorademo

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import io.agora.ConnectionListener
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import io.agora.chat.ChatOptions
import io.agora.chat.TextMessageBody
import java.text.SimpleDateFormat
import java.util.*

private const val APP_KEY = "41843811#1039979"
fun Context.initAgoraSDK(){
    val options = ChatOptions()
    // Gets your App Key from Agora Console.
    if (TextUtils.isEmpty(APP_KEY)) {
        Toast.makeText(
            this,
            "You should set your AppKey first!",
            Toast.LENGTH_SHORT
        ).show()
        return
    }
    // Sets your App Key to options.
    options.appKey = APP_KEY
    // Initializes the Agora Chat SDK.
    ChatClient.getInstance().init(this, options)
    // Makes the Agora Chat SDK debuggable.
    ChatClient.getInstance().setDebugMode(true)
    initListener()
}

private fun Context.initListener() {
    // Adds message event callbacks.
    /*ChatClient.getInstance().chatManager().addMessageListener { messages: List<ChatMessage> ->
        for (message in messages) {
            val builder = StringBuilder()
            builder.append("Receive a ").append(message.type.name)
                .append(" message from: ").append(message.from)
            if (message.type == ChatMessage.Type.TXT) {
                builder.append(" content:")
                    .append((message.body as TextMessageBody).message)
            }
            showLog(builder.toString(), false)
        }
    }*/
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
// Shows logs.
private fun Context.showLog(content: String, showToast: Boolean) {
    if (TextUtils.isEmpty(content)) {
        return
    }
    Log.e("AgoraLogs", "gr=${content}")

}