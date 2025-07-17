package com.kamleshpractical.repository

import com.google.firebase.auth.FirebaseAuth
import com.kamleshpractical.navigation.AuthResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    fun loginOrRegister(email: String, password: String): Flow<AuthResult> = callbackFlow {
        trySend(AuthResult.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                trySend(AuthResult.Success(it.user))
            }
            .addOnFailureListener {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        trySend(AuthResult.Success(it.user))
                    }
                    .addOnFailureListener { e ->
                        trySend(AuthResult.Error(e.message ?: "Unknown error"))
                    }
            }

        awaitClose { close() }
    }

}