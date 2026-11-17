package edu.virginia.cs4720.dijo.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import edu.virginia.cs4720.dijo.ui.theme.DiJoTheme

class LoginActivity : ComponentActivity() {

    private val navigator: PostLoginNavigator = DefaultPostLoginNavigator

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(FirebaseAuthRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Already signed in (Firebase persists the session): skip the login screen entirely.
        if (viewModel.isSignedIn()) {
            navigator.openHome(this)
            return
        }

        enableEdgeToEdge()
        setContent {
            DiJoTheme {
                LoginRoute(
                    viewModel = viewModel,
                    onAuthenticated = { navigator.openHome(this@LoginActivity) },
                )
            }
        }
    }
}
