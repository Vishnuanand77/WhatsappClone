package com.vishnu.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    //Firebase Variable Instantiations
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        //Getting firebase instance
        mAuth = FirebaseAuth.getInstance()

        //Create Account button listener
        CA_createAccountButton.setOnClickListener {
            //Getting User Input
            val username = CA_usernameEditText.text.toString()
            val email = CA_SignUpemailIdEditText.text.toString().trim()
            val password = loginScreenPasswordEditText.text.toString().trim()

            //Validating null input. Move to authentication only if text input is not empty
            if (!TextUtils.isEmpty(username) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                createAccount(username, email, password)
            } else {
                Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Function to create a new account
    private fun createAccount(username: String, email: String, password: String) {
        //Signing users up with email and password
        mAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
                    val currentUser = mAuth!!.currentUser
                    val userId = currentUser!!.uid

                    //Firebase Database instance
                    mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                    //A simple hashmap of a user
                    val userObject = HashMap<String, String>()
                    userObject["username"] = username
                    userObject["status"] = "Hello There"
                    userObject["image"] = "default"
                    userObject["thumbnail"] = "default"

                    //Checking if the data was added to the database or not
                    mDatabase!!.setValue(userObject).addOnCompleteListener {
                        task: Task<Void> ->
                            if (task.isSuccessful) {
                                Log.d("Firebase Database: success ", "DB entry successful")
                                //Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()

                                //Starting a new activity on success
                                val dashboardIntent = Intent(this, DashboardActivity::class.java)
                                //Extras
                                dashboardIntent.putExtra("username", username)

                                //Starting the activity
                                startActivity(dashboardIntent)
                                finish() //Ending the current activity
                            } else {
                                Log.d("Firebase Database: failure ", task.exception.toString())
                                Toast.makeText(this, "Database failed", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Log.d("Firebase Auth Error ", task.exception.toString())
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}