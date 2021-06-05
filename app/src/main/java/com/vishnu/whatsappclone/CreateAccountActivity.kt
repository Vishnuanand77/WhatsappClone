package com.vishnu.whatsappclone

import android.content.ContentValues.TAG
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    //Variable Instantiations
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        //Getting firebase instance
        mAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Auth
        auth = Firebase.auth

        //Create Account button listener
        CA_createAccountButton.setOnClickListener {
            //Getting User Input
            var username = CA_usernameEditText.text.toString()
            var email = CA_SignUpemailIdEditText.text.toString().trim()
            var password = CA_passwordEditText.text.toString().trim()

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
        mAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
                    var currentUser = mAuth!!.currentUser
                    var userId = currentUser!!.uid

                    mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                    var userObject = HashMap<String, String>()
                    userObject.put("username", username)
                    userObject.put("status", "Hello There")
                    userObject.put("image", "default")
                    userObject.put("thumbnail", "default")

                    mDatabase!!.setValue(userObject).addOnCompleteListener {
                        task: Task<Void> ->
                            if (task.isSuccessful) {
                                Log.d("Firebase Database: success ", "DB entry successful")
                                Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
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

//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser
//
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//
//                }
//            }
    }
}