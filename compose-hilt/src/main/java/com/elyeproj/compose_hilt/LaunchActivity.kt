package com.elyeproj.compose_hilt

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.elyeproj.compose_hilt.MainActivity.Companion.KEY
import com.elyeproj.compose_hilt.ui.theme.ViewModelHiltInjectionTheme

class LaunchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewModelHiltInjectionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { startActivity() }
                        ) {
                            Text("Click")
                        }
                    }
                }
            }
        }
    }

    private fun startActivity() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                putExtra(KEY, "From Launch")
            }
        )

    }
}