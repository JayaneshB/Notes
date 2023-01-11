package com.project.notes

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import com.project.notes.databinding.ActivityAddNewBinding
import com.project.notes.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val text = SpannableString( binding.splashText.text.toString())
        val fColor = ForegroundColorSpan(Color.YELLOW)
        text.setSpan(fColor,0,1,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        val sColor = ForegroundColorSpan(Color.WHITE)
        text.setSpan(sColor,1,2,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        val tColor = ForegroundColorSpan(Color.RED)
        text.setSpan(tColor,2,3,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        val foColor = ForegroundColorSpan(Color.MAGENTA)
        text.setSpan(foColor,3,4,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        val fiColor = ForegroundColorSpan(Color.CYAN)
        text.setSpan(fiColor,4,5,Spannable.SPAN_INCLUSIVE_INCLUSIVE)


        binding.splashText.text = text

    Handler().postDelayed({
        startActivity(Intent(this@SplashScreen,MainActivity::class.java))
        finish()
    },2000)



    }
}