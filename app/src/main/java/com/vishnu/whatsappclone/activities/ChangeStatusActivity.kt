package com.vishnu.whatsappclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vishnu.whatsappclone.R
import kotlinx.android.synthetic.main.activity_change_status.*

class ChangeStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_status)

        supportActionBar!!.title = "Change Status"

        if (intent.extras != null) {
            var currentStatus = intent.extras!!.get("status")
            changeStatus_StatusDisplay.setText(currentStatus.toString())
        }
    }
}