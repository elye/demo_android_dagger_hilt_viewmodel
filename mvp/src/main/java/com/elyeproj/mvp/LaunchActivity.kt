package com.elyeproj.mvp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.elyeproj.mvp.MainActivity.Companion.KEY

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        findViewById<Button>(R.id.btn_click).setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    putExtra(KEY, "From Launch")
                }
            )
        }
    }
}
