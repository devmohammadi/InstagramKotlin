package com.fmohammadi.instagram.activity

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fmohammadi.instagram.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_post.*
import java.util.*
import kotlin.collections.HashMap

class PostActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var imageUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        close.setOnClickListener {
            startActivity(Intent(this@PostActivity, MainActivity::class.java))
            finish()
        }

        post.setOnClickListener {
            upload()
        }

        CropImage.activity().start(this@PostActivity)
    }


    private fun upload() {
        val progressDialog = ProgressDialog(this@PostActivity)
        progressDialog.setMessage("Uploading...")
        progressDialog.show()

        if (imageUri != null) {
            val filePath = FirebaseStorage.getInstance().getReference("Posts")
                .child(System.currentTimeMillis().toString() + ".png")

            filePath.putFile(imageUri!!)
                .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                    if (task.isSuccessful) {
                        filePath.downloadUrl.addOnSuccessListener { uri: Uri? ->
                            if (uri != null) {
                                val downloadUri: Uri? = uri
                                imageUrl = downloadUri.toString()

                                if (task.isSuccessful) {
                                    val ref = FirebaseDatabase.getInstance().getReference("Posts")

                                    val postId: String? = ref.push().key
                                    val map: HashMap<String, String> = HashMap()
                                    map.put("postid", postId!!)
                                    map.put("imageurl", imageUrl!!)
                                    map.put("description", description!!.text.toString().trim())
                                    map.put("publisher", FirebaseAuth.getInstance().uid.toString())

                                    ref.child(postId).setValue(map)

                                    val mHashTagRef =
                                        FirebaseDatabase.getInstance().reference.child("HashTags")
                                    val hashTags: List<String> = description.hashtags
                                    if (!hashTags.isEmpty()) {
                                        for (tag in hashTags) {
                                            map.clear()
                                            map.put("tag", tag.toLowerCase(Locale.ROOT))
                                            map.put("postid", postId)

                                            mHashTagRef.child(tag.toLowerCase(Locale.ROOT))
                                                .child(postId)
                                                .setValue(map)
                                        }
                                    }
                                    progressDialog.dismiss()
                                    startActivity(
                                        Intent(
                                            this@PostActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this@PostActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        } else {
            Toast.makeText(this@PostActivity, "No Image Selected!!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR: ContentResolver = this.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            image_added.setImageURI(imageUri)
        } else {
            Toast.makeText(this@PostActivity, "Try Again!!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@PostActivity, MainActivity::class.java))
            finish()
        }

    }
}