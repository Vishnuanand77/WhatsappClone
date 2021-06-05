package com.vishnu.whatsappclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class CreateAccountActivity : AppCompatActivity() {

    //Variable Instantiations
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        //Getting firebase instance
        mAuth = FirebaseAuth.getInstance()
    }

    //Function to create a new account
    fun createAccount(email: String, password: String, displayName: String) {

    }
}