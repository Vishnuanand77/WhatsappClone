package com.vishnu.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.vishnu.whatsappclone.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    //Firebase variables
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? =null
    var mStorageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //Instantiating Firebase variables
        mCurrentUser = FirebaseAuth.getInstance().currentUser
        val userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Gets a snapshot of the entries against the userId
                var username = dataSnapshot.child("username").value
                var image = dataSnapshot.child("image").value
                var status = dataSnapshot.child("status").value
                var userThumbnail = dataSnapshot.child("thumbnail").value

                //Setting the UI to what we retrieved from the database
                settings_usernameDisplay.text = username.toString()
                settings_StatusDisplay.text = status.toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                //Records errors with respect to database
                Log.d("Firebase Database Error", databaseError.toString())
            }
        })

        //OnClick Listener for status card
        settings_status.setOnClickListener {
            val intent = Intent(this, ChangeStatusActivity::class.java)
            intent.putExtra("status", settings_StatusDisplay.text.toString().trim())
            startActivity(intent)
        }
    }
}