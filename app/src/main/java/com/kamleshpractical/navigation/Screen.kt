package com.kamleshpractical.navigation

import com.google.firebase.auth.FirebaseUser

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object NoteScreen : Screen("note_screen")
}

sealed class AuthResult {
    object Loading : AuthResult()
    data class Success(val user: FirebaseUser?) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
