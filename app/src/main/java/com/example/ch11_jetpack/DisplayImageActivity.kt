package com.example.ch11_jetpack

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_display_image.*


class DisplayImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        var path = intent.getStringExtra("path")
        var bitmap = BitmapFactory.decodeFile(path)
        imageView.setImageBitmap(bitmap)

        var loc = intent.getIntExtra("loc", 0)
        next_button.setOnClickListener{
            setResult(loc+1, intent)
            finish()
        }

        prev_button.setOnClickListener{
            setResult(loc-1, intent)
            finish()
        }

    }
    override fun onBackPressed(){
        setResult(100, intent)
        finish()
    }
}