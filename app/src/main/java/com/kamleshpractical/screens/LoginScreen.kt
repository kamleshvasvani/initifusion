package com.kamleshpractical.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.firebase.auth.FirebaseAuth
import com.kamleshpractical.navigation.AuthResult
import com.kamleshpractical.utils.ErrorDialog
import com.kamleshpractical.utils.Labels
import com.kamleshpractical.viewmodel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("kamlesh.vasvani145@gmail.com") }
    var password by remember { mutableStateOf("@123kamu") }
    val state by viewModel.authState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = Labels.LOGIN,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = { Text(Labels.EMAIL) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = { Text(Labels.PASSWORD) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.login(email, password)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(Labels.LOGIN)
                }

                when (state) {
                    is AuthResult.Success -> {
                        Log.d("LoginScreen", "Login/Registration success")
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        currentUser?.let {
                            Log.d("LoginScreen", "User UID: ${it.uid}")
                            Log.d("LoginScreen", "User Email: ${it.email}")
                        }
                        onLoginSuccess()
                    }

                    is AuthResult.Error -> {
                        val message = (state as AuthResult.Error).message
                        Log.e("LoginScreen", "Error: $message")
                        Text(message, color = Color.Red)
                    }

                    else -> {}
                }
            }
        }

        if (state is AuthResult.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(enabled = true, onClick = {})
                    .zIndex(1f),
                contentAlignment = Alignment.Center,

                ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        errorMessage?.let {
            ErrorDialog(message = it, onDismiss = { viewModel.clearError() })
        }
    }
}
