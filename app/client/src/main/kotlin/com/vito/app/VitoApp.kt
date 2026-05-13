package com.vito.app

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.vito.app.ui.login.LoginScreen
import com.vito.app.ui.main.MainScreen
import com.vito.app.vm.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class VitoApp : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(ComposeView(this).apply {
            setContent {
                MaterialTheme {
                    Surface(Modifier.fillMaxSize()) {
                        var isLoggedIn by remember { mutableStateOf(false) }
                        val vm: AuthViewModel = viewModel()
                        val user by vm.user.collectAsState()
                        
                        LaunchedEffect(user) {
                            isLoggedIn = user != null
                        }
                        
                        if (isLoggedIn) {
                            MainScreen(
                                onLogout = {
                                    // Handle logout - in production this would navigate to login
                                    finish()
                                }
                            )
                        } else {
                            LoginScreen(
                                onLoginSuccess = {
                                    isLoggedIn = true
                                }
                            )
                        }
                    }
                }
            }
        })
    }
}