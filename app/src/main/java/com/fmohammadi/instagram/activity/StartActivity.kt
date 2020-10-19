package com.fmohammadi.instagram.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.fmohammadi.instagram.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        linearLayout.animate().alpha(0f).duration = 1

        val animation = TranslateAnimation(0f, 0f, 0f, -1000f)
        animation.duration = 2000
        animation.fillAfter = false
        animation.setAnimationListener(MyAnimation())

        ivIcon?.animation = animation

        btnRegister.setOnClickListener {
            startActivity(
                Intent(this@StartActivity, RegisterActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }

        btnLogin.setOnClickListener {
            startActivity(
                Intent(this@StartActivity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
    }

    inner class MyAnimation : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            ivIcon.clearAnimation()
            ivIcon.visibility = View.INVISIBLE
            linearLayout.animate().alpha(1f).duration = 1000
        }

        override fun onAnimationEnd(p0: Animation?) {

        }

        override fun onAnimationRepeat(p0: Animation?) {

        }

    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            startActivity(
                Intent(this@StartActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
        }
    }
}