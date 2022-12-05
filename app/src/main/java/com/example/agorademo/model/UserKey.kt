package com.example.agorademo.model

import com.example.agorademo.utils.USER_ID_1
import com.example.agorademo.utils.USER_ID_2
import com.example.agorademo.utils.USER_ID_3
import com.example.agorademo.utils.USER_LOGIN_TOKEN
import java.lang.RuntimeException

data class UserData(
    var token: String,
    var username: String
)
val userKey1=UserData(USER_ID_1,"Key14")
val userKey2=UserData(USER_ID_2,"Key12")
val userKey3=UserData(USER_ID_3,"Key13")

fun getCurrentUser(isCurrentUser:Boolean):UserData{
    return when (USER_LOGIN_TOKEN) {
        USER_ID_1 -> {
            if (isCurrentUser){
                userKey1
            }else{
                userKey2
            }

        }
        userKey2.token -> {
            if (isCurrentUser){
                userKey2
            }else{
                userKey1
            }
        }
        userKey3.token -> {
            if (isCurrentUser){
                userKey3
            }else{
                userKey1
            }
        }
        else -> {
            throw RuntimeException("Invalid User")
        }
    }
}