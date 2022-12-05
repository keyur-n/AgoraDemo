package com.example.agorademo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.agorademo.R
import com.example.agorademo.databinding.ActivityLoginBinding
import com.example.agorademo.utils.USER_ID_1
import com.example.agorademo.utils.USER_ID_2
import com.example.agorademo.utils.USER_ID_3
import com.example.agorademo.utils.USER_LOGIN_TOKEN
import io.agora.chat.*
import io.agora.chat.GroupManager.GroupStyle


class LoginActivity : AppCompatActivity() {
    private val APP_KEY = "41843811#1039979"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        initSDK()
        binding.btnSignIn.setOnClickListener {

            USER_LOGIN_TOKEN = if (binding.rbUser1.isChecked) {
                USER_ID_1
            } else if (binding.rbUser2.isChecked) {
                USER_ID_2
            } else {
                USER_ID_3
            }
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        binding.btnCreateGroup.setOnClickListener {
            val groupName = binding.etGroup.text.toString()
            if (groupName.isEmpty()) {
                return@setOnClickListener
            }
            createChatGroup(groupName, "My $groupName, Lets join here.")
        }
        binding.btnJoinGroup.setOnClickListener {
            joinGroup()
        }
    }

    private fun createChatGroup(groupName: String, desc: String) {
        val option = GroupOptions()
// Set the size of a chat group to 100 members.
// Set the size of a chat group to 100 members.
        option.maxUsers = 100
// Set the type of a chat group to private. Allow chat group members to invite other users to join the chat group.
// Set the type of a chat group to private. Allow chat group members to invite other users to join the chat group.
        option.style = GroupStyle.GroupStylePublicOpenJoin

// Call createGroup to create a chat group.

// Call createGroup to create a chat group.
        var myArray1 = arrayOf(USER_ID_1, USER_ID_2, USER_ID_3)

        val gr: Group = ChatClient.getInstance().groupManager()
            .createGroup(groupName, desc, myArray1, "New group", option)
        Log.e("GroupInfo", "gr=${gr.groupId}")

// Call destroyGroup to disband a chat group.


// Call destroyGroup to disband a chat group.
//        ChatClient.getInstance().groupManager().destroyGroup(groupId)
    }

    private var cursor: String? = null
    private fun joinGroup() {
        val result =
            ChatClient.getInstance().groupManager().getPublicGroupsFromServer(10, cursor)
        val groupsList = result.data
        val returnGroups = result.data

        ChatClient.getInstance().groupManager().joinGroup(groupsList[0].groupId);

        cursor = result.cursor
    }

    private fun initSDK() {
        val options = ChatOptions()
        // Gets your App Key from Agora Console.
        if (TextUtils.isEmpty(APP_KEY)) {
            Toast.makeText(
                this@LoginActivity,
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
    }
}
