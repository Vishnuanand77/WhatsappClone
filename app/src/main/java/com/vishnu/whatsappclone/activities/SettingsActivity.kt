package com.vishnu.whatsappclone.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.vishnu.whatsappclone.R
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
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

        //Instantiating Firebase variables
        mCurrentUser = FirebaseAuth.getInstance().currentUser
        mStorageReference = FirebaseStorage.getInstance().reference

        val userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Gets a snapshot of the entries against the userId
                var username = dataSnapshot.child("username").value
                var image = dataSnapshot.child("image").value.toString()
                Log.d("Firebase Data Store", image.toString())
                var status = dataSnapshot.child("status").value
                var userThumbnail = dataSnapshot.child("thumbnail").value

                //Setting the UI to what we retrieved from the database
                settings_usernameDisplay.text = username.toString()
                settings_StatusDisplay.text = status.toString()

                //Retrieve image

                val userImageRef = FirebaseStorage.getInstance().reference
                    .child("user_profile_images/${mCurrentUser!!.uid}.jpg")

                val localFile = File.createTempFile("tempImage", "jpg")
                userImageRef.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    settings_profileImage.setImageBitmap(bitmap)
                }. addOnFailureListener {
                    Log.d("Firebase Storage", "Could not get image")
                }

//                Glide.with(this@SettingsActivity)
//                    .load(userImageRef)
//                    .into(settings_profileImage)


//                if (image != "default") {
//                    Picasso.get()
//                        .load(image)
//                        .placeholder(R.drawable.profile_logo)
//                        .into(settings_profileImage)
//                }
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
                var thumbnail = File(resultUri.path)

                var thumbBitmap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(60)
                    .compressToBitmap(thumbnail)

                //Upload image to firebase
                var  userId = mCurrentUser!!.uid
                var byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
                var thumbByteArray: ByteArray
                thumbByteArray = byteArray.toByteArray()

                var filePath = mStorageReference!!.child("user_profile_images").child("$userId.jpg")

                //Creating another directory for smaller compressed images
                var thumbFilePath = mStorageReference!!.child("user_profile_images").child("thumbnails").child("$userId.jpg")

                filePath.putFile(resultUri).addOnCompleteListener {
                    task: Task<UploadTask.TaskSnapshot> ->
                    if (task.isSuccessful) {
                        //Get the image url
                        var downloadUrl = filePath.downloadUrl.toString()

                        //Upload task
                        var uploadTask: UploadTask = thumbFilePath.putBytes(thumbByteArray)
                        uploadTask.addOnCompleteListener {
                            task : Task<UploadTask.TaskSnapshot> ->
                            var thumbUrl = thumbFilePath.downloadUrl.toString()
                            if (task.isSuccessful) {
                                var updateObject = HashMap<String, Any>()
                                updateObject["image"] = downloadUrl
                                updateObject["thumb_image"] = thumbUrl
                                
                                //Save profile image
                                mDatabase!!.updateChildren(updateObject).addOnCompleteListener { 
                                    task: Task<Void> -> 
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Profile Image Updated", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.d("Database Error", task.exception.toString())
                                    }
                                }
                            } else {
                                Log.d("Database Error", task.exception.toString())
                            }
                        }
                    }
                }
            }

        }


    }

}

