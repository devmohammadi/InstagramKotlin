package com.fmohammadi.instagram.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fmohammadi.instagram.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        register_user.setOnClickListener {
            startActivity(
                Intent(this@LoginActivity, RegisterActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }

        btnLogin.setOnClickListener {
            val txtEmail: String = etEmail.text.toString().trim()
            val txtPassword: String = etPassword.text.toString().trim()

            if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(this@LoginActivity, "Email or Password is Empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                loginUser(txtEmail, txtPassword)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this@LoginActivity, "Login is Successful", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
            }
            ?.addOnFailureListener {
                Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
            }

    }
}