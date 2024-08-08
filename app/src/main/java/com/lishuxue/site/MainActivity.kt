package com.lishuxue.site

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<View>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MPActivity::class.java)
            // 原生Activity，跳转到小程序的Activity
            startActivity(intent)
        }
    }
}