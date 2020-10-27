package com.fmohammadi.instagram.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fmohammadi.instagram.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    private var mRootRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mRootRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        pd = ProgressDialog(this)

        login_user.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            val txtUserName: String = etUserName.text.toString().trim()
            val txtName: String = etName.text.toString().trim()
            val txtEmail: String = etEmail.text.toString().trim()
            val txtPassword: String = etPassword.text.toString().trim()
            if (TextUtils.isEmpty(txtUserName) ||
                TextUtils.isEmpty(txtName) ||
                TextUtils.isEmpty(txtEmail) ||
                TextUtils.isEmpty(txtPassword)
            ) {
                Toast.makeText(
                    this@RegisterActivity,
                    "please enter information",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (txtPassword.length < 6) {
                Toast.makeText(
                    this@RegisterActivity,
                    "your password must be at least 6 characters long",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                registerUser(txtUserName, txtName, txtEmail, txtPassword)
            }
        }
    }

    private fun registerUser(userName: String, name: String, email: String, password: String) {
        pd!!.setMessage("please wait")
        pd!!.show()
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

            val map: HashMap<String, String> = HashMap()
            map.put("name", name)
            map.put("email", email)
            map.put("username", userName)
            map.put("uid", mAuth!!.currentUser!!.uid)
            map.put("bio" , "")
            map.put("imageUrl" , "")

            mRootRef!!.child("Users").child(mAuth!!.currentUser!!.uid).setValue(map)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        pd!!.dismiss()
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register is Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener {
                    pd!!.dismiss()
                    Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}