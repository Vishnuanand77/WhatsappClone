package com.vishnu.whatsappclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.vishnu.whatsappclone.R
import kotlinx.android.synthetic.main.activity_change_status.*

class ChangeStatusActivity : AppCompatActivity() {

    //Firebase Variables
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null

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
            if (!TextUtils.isEmpty(newStatus)) {
                //UI change
                changeStatus_StatusDisplay.text = newStatus.trim()

                //Adding to database
                mCurrentUser = FirebaseAuth.getInstance().currentUser
                val userId = mCurrentUser!!.uid
                mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                mDatabase!!.child("status").setValue(newStatus).addOnCompleteListener {
                    task -> if (task.isSuccessful) {
                    Toast.makeText(this, "Status Updated!", Toast.LENGTH_SHORT).show()
                    changeStatus_EditText.text.clear()
                    changeStatus_EditText.clearFocus()
                    } else {
                        Log.d("Change Status Activity Error ", task.exception.toString())
                        Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(this, "Pick a new status", Toast.LENGTH_SHORT).show()
            }

        }
    }
}