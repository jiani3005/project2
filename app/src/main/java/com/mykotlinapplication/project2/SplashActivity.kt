package com.mykotlinapplication.project2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val thread = Thread() {
            kotlin.run {
                Thread.sleep(2000)
            }

            startActivity(Intent(this, MainActivity::class.java))

        }

        thread.start()
    }
}
