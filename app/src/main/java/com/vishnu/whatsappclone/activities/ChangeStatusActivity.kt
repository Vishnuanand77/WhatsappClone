package com.vishnu.whatsappclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.vishnu.whatsappclone.R
import kotlinx.android.synthetic.main.activity_change_status.*

class ChangeStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_status)

        supportActionBar!!.title = "Change Status"

        if (intent.extras != null) {
            val currentStatus = intent.extras!!.get("status")
            changeStatus_StatusDisplay.text = currentStatus.toString()
        }
        if (intent.extras!!.equals(null)) {
            changeStatus_StatusDisplay.setText(R.string.default_status_display)
        }

        changeStatusButton.setOnClickListener {
            //Text field authentication
            val newStatus = changeStatus_EditText.text.toString()
            if (newStatus != "") {
                changeStatus_StatusDisplay.text = newStatus
            } else {
                Toast.makeText(this, "Pick a new status", Toast.LENGTH_SHORT).show()
            }
        }
    }
}