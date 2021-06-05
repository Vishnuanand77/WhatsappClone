package com.vishnu.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Firebase Variables
    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase Instantiations
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth -> user = firebaseAuth.currentUser
                if (user != null) {
                    //Autologin
                    Log.d("Auto Login ", "Auto login successful.")
                    Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                    //finish()
                } else {
                    Log.d("Auto Login ", "Auto login could not be done.")
                }
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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

    override fun onStart() {
        super.onStart()
        //Adding the auth listener on start
        Log.d("Auto Login ", "AuthListener started.")
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()

        if (mAuthListener != null) {
            Log.d("Auto Login ", "AuthListener stopped.")
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }
}