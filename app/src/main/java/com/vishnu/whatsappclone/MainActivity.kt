package com.vishnu.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton.setOnClickListener {
            buttonTest(1)
        }

        createAccountButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
            //buttonTest(2)
        }

    }

    private fun buttonTest(buttonNum : Int) {
        if (buttonNum == 1) {
            val intent = Intent(this, LoginActivity::class.java)
            Log.d("Activity changed ", "Login Activity")
            startActivity(intent)
        }
        if (buttonNum == 2) {
            val intent = Intent(this, CreateAccountActivity::class.java)
            Log.d("Activity changed ", "Create Account Activity")
            startActivity(intent)
        }

    }
}