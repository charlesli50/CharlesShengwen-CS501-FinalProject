package com.example.charlesshengwen_finalproject

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import com.example.charlesshengwen_finalproject.HomeScreen
import com.example.charlesshengwen_finalproject.auth.GoogleSignInScreen
import com.google.firebase.auth.FirebaseAuth


@Composable
fun CalorieApp(modifier: Modifier = Modifier) {

    val auth = remember{
        FirebaseAuth.getInstance()
    }

    var isSignedIn by remember { mutableStateOf(false) }

    if (isSignedIn) {
        HomeScreen()
    } else {
        GoogleSignInScreen(auth = auth, onSignInSuccess = {
            isSignedIn = true // Update the state
        })
    }
}
