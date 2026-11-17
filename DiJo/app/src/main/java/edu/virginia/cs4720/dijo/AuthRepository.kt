package edu.virginia.cs4720.dijo.login

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Helper file: the only place that knows about Firebase Auth.
 * The ViewModel depends on the [AuthRepository] interface, so you can swap in a fake for tests.
 */
enum class AuthError { INVALID_CREDENTIALS, EMAIL_IN_USE, WEAK_PASSWORD, NETWORK, UNKNOWN }

sealed interface AuthResult {
    data object Success : AuthResult
    data class Failure(val error: AuthError) : AuthResult
}

interface AuthRepository {
    val isSignedIn: Boolean
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signUp(email: String, password: String): AuthResult
    fun signOut()
}

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : AuthRepository {

    override val isSignedIn: Boolean
        get() = auth.currentUser != null

    override suspend fun signIn(email: String, password: String): AuthResult =
        attempt { auth.signInWithEmailAndPassword(email, password).awaitTask() }

    override suspend fun signUp(email: String, password: String): AuthResult =
        attempt { auth.createUserWithEmailAndPassword(email, password).awaitTask() }

    override fun signOut() = auth.signOut()

    private suspend fun attempt(block: suspend () -> Unit): AuthResult =
        try {
            block()
            AuthResult.Success
        } catch (e: CancellationException) {
            throw e // never swallow coroutine cancellation
        } catch (e: Exception) {
            AuthResult.Failure(e.toAuthError())
        }
}

private fun Exception.toAuthError(): AuthError = when (this) {
    // Weak password is a subclass of invalid credentials, so it must be checked first.
    is FirebaseAuthWeakPasswordException -> AuthError.WEAK_PASSWORD
    is FirebaseAuthUserCollisionException -> AuthError.EMAIL_IN_USE
    is FirebaseAuthInvalidCredentialsException,
    is FirebaseAuthInvalidUserException -> AuthError.INVALID_CREDENTIALS
    is FirebaseNetworkException -> AuthError.NETWORK
    else -> AuthError.UNKNOWN
}

/** Bridges a Google Play services Task to a suspend function without extra dependencies. */
private suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task ->
        when {
            task.isSuccessful -> cont.resume(task.result)
            task.isCanceled -> cont.cancel()
            else -> cont.resumeWithException(
                task.exception ?: IllegalStateException("Firebase task failed without an exception")
            )
        }
    }
}
