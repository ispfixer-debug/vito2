package com.vito.app.admin
import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView

class VitoAdminApp : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(ComposeView(this).apply {
            setContent {
                MaterialTheme {
                    Surface(Modifier.fillMaxSize()) {
                        Column(Modifier.padding(24.dp)) {
                            Text("Vito Admin", style = MaterialTheme.typography.headlineLarge)
                        }
                    }
                }
            }
        })
    }
}
