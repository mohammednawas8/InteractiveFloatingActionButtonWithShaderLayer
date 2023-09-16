package com.loc.shaderfab

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

val floatingActionButtonOptions = listOf(
    FloatingActionButtonOption(
        icon = R.drawable.ic_share,
        text = "Share",
        tintColor = blue,
        containerColor = Color.White,
        textColor = Color.White
    ),
    FloatingActionButtonOption(
        icon = R.drawable.ic_photo,
        text = "Photos",
        tintColor = blue,
        containerColor = Color.White,
        textColor = Color.White
    ),

    FloatingActionButtonOption(
        icon = R.drawable.ic_gif,
        text = "Gif",
        tintColor = blue,
        containerColor = Color.White,
        textColor = Color.White
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWithFAB(paddingValues: PaddingValues) {
    var showOptions: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            InteractiveFloatingActionButton(
                mainIcon = R.drawable.ic_add,
                mainIconAlternative = R.drawable.ic_tweet,
                mainText = "Tweet",
                options = floatingActionButtonOptions,
                onMainIconClick = {
                    showOptions = !showOptions
                },
                showOptions = showOptions,
                onOptionClick = {
                    when(it){
                        floatingActionButtonOptions[0] -> {
                            // Share Option Click
                        }
                        floatingActionButtonOptions[0] -> {
                            // Photos Option Click
                        }
                        floatingActionButtonOptions[0] -> {
                            // Gif Option Click
                        }
                    }
                },
                textStyle = TextStyle(color = Color.White),
                dismissFABOptionsRequest = {
                    showOptions = false
                },
                sizes = FloatingActionButtonSizes(mainFloatingActionButton = 60.dp)
            )
        }
    ) {
        it.calculateBottomPadding()
    }
}