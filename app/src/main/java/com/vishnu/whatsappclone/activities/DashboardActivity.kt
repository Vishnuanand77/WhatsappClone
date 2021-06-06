package com.vishnu.whatsappclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vishnu.whatsappclone.R

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.title = "Dashboard"
    }
}