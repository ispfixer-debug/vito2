package com.vito.app.driver
import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView

class VitoDriverApp : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(ComposeView(this).apply {
            setContent {
                MaterialTheme {
                    Surface(Modifier.fillMaxSize()) {
                        Column(Modifier.padding(24.dp)) {
                            Text("Vito Driver", style = MaterialTheme.typography.headlineLarge)
                        }
                    }
                }
            }
        })
    }
}
