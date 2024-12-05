package com.example.charlesshengwen_finalproject

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import com.example.charlesshengwen_finalproject.HomeScreen
import com.example.charlesshengwen_finalproject.auth.GoogleSignInScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

// front end
import com.example.charlesshengwen_finalproject.auth.WelcomeScreen

@Composable
fun CalorieApp(modifier: Modifier = Modifier) {


    val auth = remember {
        FirebaseAuth.getInstance()
    }

    var isSignedIn by remember { mutableStateOf(false) }
    var googleSignInClient by remember { mutableStateOf<GoogleSignInClient?>(null) }

    if (isSignedIn) {
        googleSignInClient?.let { client ->
            HomeScreen(auth = auth, googleSignInClient = client, modifier = modifier.padding(), onSignOut = {
                isSignedIn = false // Update the state
                googleSignInClient = null // Clear the GoogleSignInClient
            })
        }
    } else {
        WelcomeScreen(
            auth = auth,
            onSignInSuccess = { client ->
                isSignedIn = true // Update the state
                googleSignInClient = client // Capture the GoogleSignInClient
            }
        )
    }
}