package com.example.agorademo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.agorademo.databinding.ActivityLoginBinding
import com.example.agorademo.utils.USER_ID_1
import com.example.agorademo.utils.USER_ID_2
import com.example.agorademo.utils.USER_LOGIN_TOKEN

class LoginActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityLoginBinding=DataBindingUtil.setContentView(this,R.layout.activity_login)

        binding.btnSignIn.setOnClickListener {

            USER_LOGIN_TOKEN = if (binding.rbUser1.isChecked){
                USER_ID_1
            }else{
                USER_ID_2
            }
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        }
    }
}