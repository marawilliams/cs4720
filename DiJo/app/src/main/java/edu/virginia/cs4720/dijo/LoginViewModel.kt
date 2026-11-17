package edu.virginia.cs4720.dijo.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AuthAction { LOG_IN, SIGN_UP }

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val activeAction: AuthAction? = null,
    val errorMessage: String? = null,
) {
    val isLoading: Boolean get() = activeAction != null

    /** Every rule paired with whether the current password satisfies it. Drives the live checklist. */
    val passwordRules: List<Pair<PasswordRule, Boolean>>
        get() = PasswordRule.entries.map { it to it.isSatisfiedBy(password) }

    // Existing accounts may predate the policy, so logging in only needs non-empty fields.
    val canLogIn: Boolean get() = !isLoading && email.isNotBlank() && password.isNotEmpty()

    // New accounts must meet the full password policy and have a plausible email.
    val canSignUp: Boolean
        get() = !isLoading && EmailValidator.isValid(email) && PasswordPolicy.isValid(password)
}

/** One-shot events. The screen reacts to these; the ViewModel never knows what comes next. */
sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
}

class LoginViewModel(
    private val repository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    fun isSignedIn(): Boolean = repository.isSignedIn

    fun onEmailChange(value: String) {
        // Emails never contain whitespace, so drop it as the user types or pastes.
        _uiState.update { it.copy(email = value.filterNot(Char::isWhitespace), errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onLogInClick() {
        val state = _uiState.value
        if (!state.canLogIn) return
        if (!EmailValidator.isValid(state.email)) {
            _uiState.update { it.copy(errorMessage = "Enter a valid email address.") }
            return
        }
        submit(AuthAction.LOG_IN) { repository.signIn(state.email, state.password) }
    }

    fun onSignUpClick() {
        val state = _uiState.value
        if (!state.canSignUp) return
        submit(AuthAction.SIGN_UP) { repository.signUp(state.email, state.password) }
    }

    private fun submit(action: AuthAction, request: suspend () -> AuthResult) {
        _uiState.update { it.copy(activeAction = action, errorMessage = null) }
        viewModelScope.launch {
            when (val result = request()) {
                AuthResult.Success -> {
                    _uiState.update { it.copy(activeAction = null) }
                    _events.send(LoginEvent.NavigateToHome)
                }
                is AuthResult.Failure -> _uiState.update {
                    it.copy(activeAction = null, errorMessage = result.error.toMessage())
                }
            }
        }
    }
}

private fun AuthError.toMessage(): String = when (this) {
    AuthError.INVALID_CREDENTIALS -> "Email or password is incorrect."
    AuthError.EMAIL_IN_USE -> "An account with this email already exists. Try logging in."
    AuthError.WEAK_PASSWORD -> "Firebase rejected that password as too weak. Try a stronger one."
    AuthError.NETWORK -> "Can't reach the server. Check your connection and try again."
    AuthError.UNKNOWN -> "Something went wrong. Please try again."
}

/** Manual dependency injection: the Activity supplies the repository. */
class LoginViewModelFactory(
    private val repository: AuthRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            "Unknown ViewModel class: ${modelClass.name}"
        }
        return LoginViewModel(repository) as T
    }
}
