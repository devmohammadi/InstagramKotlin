package com.fmohammadi.instagram

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    private var mRootRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mRootRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        login_user.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
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
            } else if (txtPassword.length > 6) {
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
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnSuccessListener { }
    }
}