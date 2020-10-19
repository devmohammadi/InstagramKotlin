package com.fmohammadi.instagram.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fmohammadi.instagram.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var imageUrl: String? = null

    private val description: SocialAutoCompleteTextView? = null

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
                .child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!))

            val uploadTask = filePath.getFile(imageUri!!)
            uploadTask.continueWithTask({
                if (!it.isSuccessful) {
                    throw it.exception!!
                }
                return@continueWithTask filePath.downloadUrl
            }).addOnCanceledListener{
                OnCompleteListener<Uri> {
                    val downloadUri: Uri? = it.result
                    imageUrl = downloadUri.toString()

                    val ref = FirebaseStorage.getInstance().getReference("Posts")
                }
            }
        }

    }

    private fun getFileExtension(uri: Uri): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            imahe_added.setImageURI(imageUri)
        } else {
            Toast.makeText(this@PostActivity, "Try Again!!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@PostActivity, MainActivity::class.java))
            finish()
        }

    }
}