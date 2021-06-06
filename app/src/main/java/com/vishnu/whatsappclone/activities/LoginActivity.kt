package com.vishnu.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.vishnu.whatsappclone.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    //Firebase Variable Instantiations
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Getting firebase instance
        mAuth = FirebaseAuth.getInstance()

        loginScreenLoginButton.setOnClickListener {
            val emailId = loginScreenEmailIdEditText.text.toString().trim()
            val password = loginScreenPasswordEditText.text.toString().trim()

            if (!TextUtils.isEmpty(emailId) || !TextUtils.isEmpty(password)) {
                loginUser(emailId, password)
            } else {
                Toast.makeText(this, "You missed a field!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //Login function
    private fun loginUser(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            task -> if (task.isSuccessful) {
                Log.d("User Login ", "Login Successful")

                //Starting a new activity on success
                val dashboardIntent = Intent(this, DashboardActivity::class.java)
                //Extras
                dashboardIntent.putExtra("username", email)

                //Starting the activity
                startActivity(dashboardIntent)
                finish() //Ending the current activity
            } else {
                Log.d("User Login", task.exception.toString())
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }
        }
    }
}