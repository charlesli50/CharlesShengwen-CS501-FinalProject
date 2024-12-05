package com.example.charlesshengwen_finalproject.auth

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.installations.BuildConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// front end
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable



private const val SERVER_CLIENT_ID = "SERVER_API_KEY"
const val SIGN_IN_WITH_GOOGLE = "Sign in with Google"




@Composable
fun GoogleSignInScreen(auth: FirebaseAuth, onSignInSuccess: (GoogleSignInClient) -> Unit) {
    var showSignIn by remember { mutableStateOf(false) }
    var isSigningIn by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            isSigningIn = false
            Log.w(TAG, "Google sign-in failed with result code: ${result.resultCode}")
            return@rememberLauncherForActivityResult
        }

        val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
        val oneTapClient = Identity.getSignInClient(context)
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken
        if (idToken != null) {
            firebaseAuthWithGoogle(auth, idToken) { onSignInSuccess(googleSignInClient) }
        } else {
            Log.w(TAG, "Google sign-in failed: No ID token!")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Welcome Text Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Start or sign in to your account",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (!showSignIn) {
                // Start Button
                Button(
                    onClick = { showSignIn = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F3C)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start")
                }
            }
        }

        if (showSignIn) {
            // Dimmed background with clickable to revert to the Start screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showSignIn = false }
            )

            // "Sign in with Google" Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isSigningIn) {
                    CircularProgressIndicator(color = Color.Green)
                } else {
                    GoogleSignInButton(onClick = {
                        scope.launch {
                            isSigningIn = true
                            try {
                                signIn(context, launcher)
                            } catch (e: Exception) {
                                isSigningIn = false
                                Log.w(TAG, "Google sign-in failed", e)
                            }
                        }
                    })
                }
            }
        }
    }
}






@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F3C)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = SIGN_IN_WITH_GOOGLE,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = SIGN_IN_WITH_GOOGLE)
    }
}


private fun firebaseAuthWithGoogle(
    auth: FirebaseAuth,
    idToken: String,
    onSignInSuccess: () -> Unit
) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithCredential:success")
                onSignInSuccess()
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
            }
        }
}

suspend fun signIn(
    context: Context,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(SERVER_CLIENT_ID)  // Replace with your actual client ID
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    try {
        // Use await() to handle sign-in asynchronously
        val result = oneTapClient.beginSignIn(signInRequest).await()

        // Launch the result using the launcher
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
        // Handle exceptions (e.g., no saved credentials or failed request)
        Log.d(TAG, "Sign-in failed: ${e.message}")
    }
}


fun signOut(googleSignInClient: GoogleSignInClient, auth: FirebaseAuth, onSignOut: () -> Unit) {
    auth.signOut()
    googleSignInClient.signOut().addOnCompleteListener {
        Log.d(TAG, "signOut:success")
        onSignOut()
    }
}