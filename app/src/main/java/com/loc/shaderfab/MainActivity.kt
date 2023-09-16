package com.loc.shaderfab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.loc.shaderfab.ui.theme.ShaderFABTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShaderFABTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    ParentScreen()
                }
            }
        }
    }
}













