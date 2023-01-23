package com.project.notes

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import com.project.notes.databinding.ActivityAddNewBinding
import com.project.notes.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animation = AnimationUtils.loadAnimation(this@SplashScreen,R.anim.splash_text_animation)

        binding.splashText.startAnimation(animation)

    Handler().postDelayed({
        startActivity(Intent(this@SplashScreen,MainActivity::class.java))
        finish()
    },2000)

    }
}