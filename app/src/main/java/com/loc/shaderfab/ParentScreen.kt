package com.loc.shaderfab

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentScreen(
) {
    Scaffold(
        topBar = {
            TopAppBarSample()
        },
        bottomBar = {
            BottomAppBarSample()
        }
    ) {

        ScreenWithFAB(
            PaddingValues(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            )
        )
    }
}


@Composable
fun BottomAppBarSample() {
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(1f)
        ) {
            Text(text = "Item")
        }
        TextButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
            Text(text = "Item")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSample() {
    TopAppBar(
        title = {
            Text(text = "Top App Bar")
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}