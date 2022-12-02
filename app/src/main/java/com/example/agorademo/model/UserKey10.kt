package com.example.agorademo.model

import com.example.agorademo.utils.USER_ID_1
import com.example.agorademo.utils.USER_ID_2
import com.example.agorademo.utils.USER_LOGIN_TOKEN
import java.lang.RuntimeException

data class UserKey10(
    val key: String = USER_ID_1,
    val username: String = "Key10"
)
data class UserKey11(
    val key: String = USER_ID_2,
    val username: String = "Key11"
)
data class UserData(
    var token: String,
    var username: String
)
val userKey10=UserData(USER_ID_1,"Key10")
val userKey11=UserData(USER_ID_2,"Key11")
fun getCurrentUser():UserData{
    return when (USER_LOGIN_TOKEN) {
        userKey10.token -> {
            userKey10
        }
        userKey11.token -> {
            userKey11
        }
        else -> {
            throw RuntimeException("Invalid User")
        }
    }
}
fun getCurrentUser(isCurrentUser:Boolean):UserData{
    return when (USER_LOGIN_TOKEN) {
        userKey10.token -> {
            if (isCurrentUser){
                userKey10
            }else{
                userKey11
            }

        }
        userKey11.token -> {
            if (isCurrentUser){
                userKey11
            }else{
                userKey10
            }
        }
        else -> {
            throw RuntimeException("Invalid User")
        }
    }
}