package com.example.agorademo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agorademo.R
import com.example.agorademo.databinding.ActivityHomeBinding
import com.example.agorademo.initAgoraSDK
import com.example.agorademo.model.ChatListData
import com.example.agorademo.model.getCurrentUser
import com.example.agorademo.model.userKey3
import com.example.agorademo.ui.adapter.ChatListAdapter
import io.agora.GroupChangeListener
import io.agora.chat.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity:AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var list=ArrayList<ChatListData>()
    private val mAdapter by lazy { ChatListAdapter(list) }
    private val currentUser by lazy { getCurrentUser(true) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_home)
        initUI()
        initAgoraSDK()
        initClickListener()
        getAllGroups()


    }

    override fun onResume() {
        super.onResume()
        requestGroup()
//        if (currentUser.username.equals(userKey3.username)){
//            lifecycleScope.launch(Dispatchers.IO) {
//                ChatClient.getInstance().groupManager().joinGroup("199850683596801");
//            }
//
//        }
    }
    private fun initUI() {
        binding.rvChatList.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=mAdapter
        }
    }

    private fun initClickListener() {
        ChatClient.getInstance().groupManager().addGroupChangeListener(groupListener);
        /*binding.btnCreateGroup.setOnClickListener {
            val groupName = binding.etGroup.text.toString()
            if (groupName.isEmpty()) {
                return@setOnClickListener
            }
            createChatGroup(groupName, "My $groupName, Lets join here.")
        }*/
        /*binding.btnJoinGroup.setOnClickListener {
            joinGroup()
        }*/
    }
    private fun createChatGroup(groupName: String, desc: String) = lifecycleScope.launch(Dispatchers.IO){
        try {
            val option = GroupOptions()
// Set the size of a chat group to 100 members.
// Set the size of a chat group to 100 members.
            option.maxUsers = 10
// Set the type of a chat group to private. Allow chat group members to invite other users to join the chat group.
// Set the type of a chat group to private. Allow chat group members to invite other users to join the chat group.
            option.style = GroupManager.GroupStyle.GroupStylePrivateMemberCanInvite
            option.inviteNeedConfirm = false

// Call createGroup to create a chat group.

// Call createGroup to create a chat group.
            var myArray1 = arrayOf("Key14", "Key12", "Key13")

            val gr: Group = ChatClient.getInstance().groupManager()
                .createGroup(groupName, desc, myArray1, "New group", option)
            Log.e("GroupInfo", "gr=${gr.groupId}")

// Call destroyGroup to disband a chat group.


// Call destroyGroup to disband a chat group.
//        ChatClient.getInstance().groupManager().destroyGroup(groupId)
        }
        catch (e:Exception){
            Log.e("GroupInfo", "grException=${e.message}")
        }

    }
    private var cursor: String? = ""
    private fun requestGroup() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result =
                    ChatClient.getInstance().groupManager().getPublicGroupsFromServer(10, cursor)
                val groupsList = result.data
                val returnGroups = result.data
                Log.e("JoinRequest","List=${groupsList.size}")
//            ChatClient.getInstance().groupManager().joinGroup(groupsList[0].groupId);
                for (item in groupsList){
                    ChatClient.getInstance().groupManager().joinGroup(item.groupId);
                }
                cursor = result.cursor
            }catch (e:Exception){
                Log.e("JoinRequest","Exception=${e.message}")
            }

        }

    }
    private fun getAllGroups(){
        val listGroups=ChatClient.getInstance().groupManager().allGroups
        Log.e("GroupInfo","GroupJoinedCount=${listGroups.size}")
        for (item in listGroups){
            Log.e("GroupInfo","GroupJoinedCount=${item.groupName}")
            Log.e("GroupInfo","GroupJoinedCount=${item.groupId}")
            Log.e("GroupInfo","GroupJoinedCount=${item.adminList}")
            Log.e("GroupInfo","GroupJoinedCount=${item.description}")
            Log.e("GroupInfo","GroupJoinedCount=${item.extension}")
            Log.e("GroupInfo","GroupJoinedCount=${item.isMemberAllowToInvite}")
            Log.e("GroupInfo","GroupJoinedCount=${item.isPublic}")
            Log.e("GroupInfo","GroupJoinedCount=${item.maxUserCount}")
            Log.e("GroupInfo","GroupJoinedCount=${item.muteList}")
            Log.e("GroupInfo","GroupJoinedCount=${item.members}")
            Log.e("GroupInfo","GroupJoinedCount=${item.isMsgBlocked}")

        }
        val listChat=listGroups.map {
            ChatListData(isGroup = true, chatId = it.groupId,chatName=it.groupName)
        }
        list.addAll(listChat)
        mAdapter.notifyItemChanged(0,listChat)
        val allConversion=ChatClient.getInstance().chatManager().allConversations
        Log.e("GroupInfo","allConversion=${allConversion.size}")

        for (conversation in allConversion){
            val item=conversation.value
            Log.e("GroupInfo","GroupJoinedCount=${item.isGroup}")
            Log.e("GroupInfo","GroupJoinedCount=${item.allMsgCount}")
            Log.e("GroupInfo","GroupJoinedCount=${item.allMessages}")
            Log.e("GroupInfo","GroupJoinedCount=${item.type}")
            Log.e("GroupInfo","GroupJoinedCount=${item.extField}")
            Log.e("GroupInfo","GroupJoinedCount=${item.isChatThread}")
            Log.e("GroupInfo","GroupJoinedCount=${item.lastMessage}")
            Log.e("GroupInfo","GroupJoinedCount=${item.latestMessageFromOthers}")
            Log.e("GroupInfo","GroupJoinedCount=${item.unreadMsgCount}")
            Log.e("GroupInfo","GroupJoinedCount=${item.conversationId()}")
            Log.e("GroupInfo","GroupJoinedCount=${item.markAllMessagesAsRead()}")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        ChatClient.getInstance().groupManager().removeGroupChangeListener(groupListener)
    }

    private val groupListener =object: GroupChangeListener{
        override fun onInvitationReceived(
            groupId: String?,
            groupName: String?,
            inviter: String?,
            reason: String?
        ) {
            Log.e("GroupInfo", "onInvitationReceived=${groupId}")
        }

        override fun onRequestToJoinReceived(
            groupId: String?,
            groupName: String?,
            applicant: String?,
            reason: String?
        ) {
            Log.e("GroupInfo", "onRequestToJoinReceived=${groupId}")
        }

        override fun onRequestToJoinAccepted(
            groupId: String?,
            groupName: String?,
            accepter: String?
        ) {
            Log.e("GroupInfo", "onRequestToJoinAccepted=${groupId}")
        }

        override fun onRequestToJoinDeclined(
            groupId: String?,
            groupName: String?,
            decliner: String?,
            reason: String?
        ) {
            Log.e("GroupInfo", "onRequestToJoinDeclined=${groupId}")
        }

        override fun onInvitationAccepted(groupId: String?, invitee: String?, reason: String?) {
            Log.e("GroupInfo", "onInvitationAccepted=${groupId}")
        }

        override fun onInvitationDeclined(groupId: String?, invitee: String?, reason: String?) {
            Log.e("GroupInfo", "onInvitationDeclined=${groupId}")
        }

        override fun onUserRemoved(groupId: String?, groupName: String?) {
            Log.e("GroupInfo", "onUserRemoved=${groupId}")
        }

        override fun onGroupDestroyed(groupId: String?, groupName: String?) {
            Log.e("GroupInfo", "onGroupDestroyed=${groupId}")
        }

        override fun onAutoAcceptInvitationFromGroup(
            groupId: String?,
            inviter: String?,
            inviteMessage: String?
        ) {
            Log.e("GroupInfo", "onAutoAcceptInvitationFromGroup=${groupId}")

        }

        override fun onMuteListAdded(
            groupId: String?,
            mutes: MutableList<String>?,
            muteExpire: Long
        ) {
            Log.e("GroupInfo", "onMuteListAdded=${groupId}")
        }

        override fun onMuteListRemoved(groupId: String?, mutes: MutableList<String>?) {
            Log.e("GroupInfo", "onMuteListRemoved=${groupId}")
        }

        override fun onWhiteListAdded(groupId: String?, whitelist: MutableList<String>?) {
            Log.e("GroupInfo", "onWhiteListAdded=${groupId}")
        }

        override fun onWhiteListRemoved(groupId: String?, whitelist: MutableList<String>?) {
            Log.e("GroupInfo", "onWhiteListRemoved=${groupId}")
        }

        override fun onAllMemberMuteStateChanged(groupId: String?, isMuted: Boolean) {
            Log.e("GroupInfo", "onAllMemberMuteStateChanged=${groupId}")
        }

        override fun onAdminAdded(groupId: String?, administrator: String?) {
            Log.e("GroupInfo", "onAdminAdded=${groupId}")
        }

        override fun onAdminRemoved(groupId: String?, administrator: String?) {
            Log.e("GroupInfo", "onAdminRemoved=${groupId}")
        }

        override fun onOwnerChanged(groupId: String?, newOwner: String?, oldOwner: String?) {
            Log.e("GroupInfo", "onOwnerChanged=${groupId}")
        }

        override fun onMemberJoined(groupId: String?, member: String?) {
            Log.e("GroupInfo", "onMemberJoined=${groupId}")
        }

        override fun onMemberExited(groupId: String?, member: String?) {
            Log.e("GroupInfo", "onMemberExited=${groupId}")
        }

        override fun onAnnouncementChanged(groupId: String?, announcement: String?) {
            Log.e("GroupInfo", "onAnnouncementChanged=${groupId}")
        }

        override fun onSharedFileAdded(groupId: String?, sharedFile: MucSharedFile?) {
            Log.e("GroupInfo", "onSharedFileAdded=${groupId}")
        }

        override fun onSharedFileDeleted(groupId: String?, fileId: String?) {
            Log.e("GroupInfo", "onSharedFileDeleted=${groupId}")
        }

    }
    companion object{
        fun onNewIntent(context: Context){
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }
}