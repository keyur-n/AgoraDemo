package com.example.agorademo.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.agorademo.R
import com.example.agorademo.databinding.ActivityUserLoginBinding
import com.example.agorademo.initAgoraSDK
import com.example.agorademo.model.getCurrentUser
import com.example.agorademo.utils.USER_ID_1
import com.example.agorademo.utils.USER_ID_2
import com.example.agorademo.utils.USER_ID_3
import com.example.agorademo.utils.USER_LOGIN_TOKEN
import io.agora.CallBack
import io.agora.chat.ChatClient

class UserLoginActivity : AppCompatActivity() {
    private val APP_KEY = "41843811#1039979"
    private var USERNAME = "<Your username>"
    private var TOKEN = "<Your token>"
    private lateinit var binding: ActivityUserLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_login)
        initAgoraSDK()
        if (ChatClient.getInstance().isLoggedIn){
            USER_LOGIN_TOKEN=ChatClient.getInstance().accessToken
            gotoHome()
            return
        }
        binding.btnSignIn.setOnClickListener {

            USER_LOGIN_TOKEN = if (binding.rbUser1.isChecked) {
                USER_ID_1
            } else if (binding.rbUser2.isChecked) {
                USER_ID_2
            } else {
                USER_ID_3
            }
            val currentUser=getCurrentUser(true)
            USERNAME=currentUser.username
            TOKEN=currentUser.token
            loginToAgora()
        }
    }

    private fun loginToAgora() {
        if (TextUtils.isEmpty(USERNAME) || TextUtils.isEmpty(TOKEN)) {
            showLog("Username or token is empty!", true)
            return
        }

        ChatClient.getInstance().loginWithAgoraToken(USERNAME, TOKEN, object : CallBack {
            override fun onSuccess() {
                showLog("Sign in success!", true)
               gotoHome()
            }

            override fun onError(code: Int, error: String) {
                showLog(error, true)
                if (code==200){
                    gotoHome()
                }
            }
        })
    }

    private fun gotoHome() {
        HomeActivity.onNewIntent(this@UserLoginActivity)
        finish()
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
//            val tv_log = findViewById<TextView>(R.id.tv_log)
//            val preContent = tv_log.text.toString().trim { it <= ' ' }
//            val builder = java.lang.StringBuilder()
//            builder.append(
//                SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss",
//                    Locale.getDefault()
//                ).format(Date())
//            )
//                .append(" ").append(content).append("\n").append(preContent)
//            tv_log.text = builder
        }
    }
}