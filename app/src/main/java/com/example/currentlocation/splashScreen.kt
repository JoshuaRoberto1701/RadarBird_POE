package com.example.currentlocation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView

class splashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val video: VideoView = findViewById(R.id.videoView)
        val videoPath: String = "android.resource://" + packageName + "/" + R.raw.splash

        val videoUri = Uri.parse(videoPath)
        video.setVideoURI(videoUri)
        video.start()

        video.setOnCompletionListener {

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
    }
}