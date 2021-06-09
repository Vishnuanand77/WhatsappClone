package com.vishnu.whatsappclone.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.vishnu.whatsappclone.R
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File
import java.lang.Exception

class SettingsActivity : AppCompatActivity() {

    //Firebase variables
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? =null
    var mStorageReference: StorageReference? = null

    //Image variables
    var GALLERY_ID: Int = 1

    //New Method
//    private val pickImage = 100
    private var imageUri: Uri? = null

    private val SAMPLE_CROPPED_IMAGE_NAME : String = "SampleCropImg"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //Image Cropper


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

        //Image click listener
        settings_profileImage.setOnClickListener {
            Log.d("settings_profileImage.setOnClickListener ", "Method Entered")
            //Course Method
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_ID && resultCode == RESULT_OK) {
            var imageUri: Uri = data!!.data!!

            CropImage.activity(imageUri)
                .setAspectRatio(1,1)
                .start(this)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                var  userId = mCurrentUser!!.uid
                var thumbnail = File(resultUri.path)
            }
        }

    }

}

