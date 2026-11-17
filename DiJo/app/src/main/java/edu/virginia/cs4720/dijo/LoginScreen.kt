package edu.virginia.cs4720.dijo.login

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.virginia.cs4720.dijo.ui.theme.DiJoTheme

// Palette from the pitch deck. Move these into ui/theme once the app theme has tokens.
private val Cream = Color(0xFFFAF6EF)
private val Ink = Color(0xFF2A2931)
private val Teal = Color(0xFF144F5D)
private val SunYellow = Color(0xFFF7D774)
private val Hairline = Color(0xFFE4DDD0)
private val MutedText = Color(0xFF7A7784)
private val PlaceholderText = Color(0xFFA9A6B0)
private val ErrorRed = Color(0xFFB3261E)

/**
 * Stateful wrapper: connects the ViewModel to the stateless [LoginScreen] and forwards the
 * "authenticated" signal outward. It has no idea what the next page is.
 */
@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onAuthenticated: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val latestOnAuthenticated by rememberUpdatedState(onAuthenticated)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.NavigateToHome -> latestOnAuthenticated()
            }
        }
    }

    LoginScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogInClick = viewModel::onLogInClick,
        onSignUpClick = viewModel::onSignUpClick,
    )
}

/** Stateless screen: everything it shows comes from [state], everything it does is a callback. */
@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Cream)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        SmileyLogo()
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Welcome back",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            color = Ink,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Log in to keep track of your days.",
            fontSize = 14.sp,
            color = MutedText,
        )

        Spacer(Modifier.height(28.dp))
        FieldLabel("EMAIL")
        AuthTextField(
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = "you@email.com",
            accessibilityLabel = "Email",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            enabled = !state.isLoading,
        )

        Spacer(Modifier.height(16.dp))
        FieldLabel("PASSWORD")
        AuthTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = "Password",
            accessibilityLabel = "Password",
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            isPassword = true,
            enabled = !state.isLoading,
        )

        Spacer(Modifier.height(16.dp))
        PasswordRequirementsCard(rules = state.passwordRules)

        // Reserve a live region so screen readers announce errors as they appear.
        state.errorMessage?.let { message ->
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                color = ErrorRed,
                fontSize = 14.sp,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )
        }

        Spacer(Modifier.height(24.dp))
        PrimaryPillButton(
            text = "Log in",
            onClick = {
                focusManager.clearFocus()
                onLogInClick()
            },
            enabled = state.canLogIn,
            loading = state.activeAction == AuthAction.LOG_IN,
        )
        Spacer(Modifier.height(12.dp))
        SecondaryPillButton(
            text = "Create account",
            onClick = {
                focusManager.clearFocus()
                onSignUpClick()
            },
            enabled = state.canSignUp,
            loading = state.activeAction == AuthAction.SIGN_UP,
        )
    }
}

@Composable
private fun SmileyLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(52.dp)) {
        drawCircle(color = SunYellow)
        val eyeRadius = size.minDimension * 0.045f
        drawCircle(Ink, eyeRadius, Offset(size.width * 0.36f, size.height * 0.40f))
        drawCircle(Ink, eyeRadius, Offset(size.width * 0.64f, size.height * 0.40f))
        drawArc(
            color = Ink,
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(size.width * 0.28f, size.height * 0.30f),
            size = Size(size.width * 0.44f, size.height * 0.44f),
            style = Stroke(width = size.minDimension * 0.06f, cap = StrokeCap.Round),
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        color = MutedText,
        modifier = Modifier.padding(bottom = 6.dp),
    )
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    accessibilityLabel: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = accessibilityLabel },
        enabled = enabled,
        singleLine = true,
        placeholder = { Text(placeholder, color = PlaceholderText) },
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = Teal,
            unfocusedBorderColor = Hairline,
            disabledBorderColor = Hairline,
            cursorColor = Ink,
            focusedTextColor = Ink,
            unfocusedTextColor = Ink,
            disabledTextColor = MutedText,
        ),
    )
}

/** Live checklist: each row flips from an empty ring to a filled check as its rule is met. */
@Composable
private fun PasswordRequirementsCard(
    rules: List<Pair<PasswordRule, Boolean>>,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Hairline, shape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "Your password needs",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Ink,
        )
        rules.forEach { (rule, met) -> RequirementRow(label = rule.label, met = met) }
    }
}

@Composable
private fun RequirementRow(label: String, met: Boolean) {
    val badgeColor by animateColorAsState(if (met) Teal else Color.Transparent, label = "badge")
    val ringColor by animateColorAsState(if (met) Teal else Hairline, label = "ring")
    val textColor by animateColorAsState(if (met) Ink else MutedText, label = "text")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.semantics(mergeDescendants = true) {
            stateDescription = if (met) "Met" else "Not met"
        },
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(badgeColor)
                .border(1.5.dp, ringColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (met) {
                Canvas(Modifier.size(12.dp)) {
                    val stroke = size.minDimension * 0.18f
                    drawLine(
                        Color.White,
                        Offset(size.width * 0.10f, size.height * 0.55f),
                        Offset(size.width * 0.40f, size.height * 0.85f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round,
                    )
                    drawLine(
                        Color.White,
                        Offset(size.width * 0.40f, size.height * 0.85f),
                        Offset(size.width * 0.92f, size.height * 0.18f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(text = label, fontSize = 14.sp, color = textColor, textAlign = TextAlign.Start)
    }
}

@Composable
private fun PrimaryPillButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    loading: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Ink,
            contentColor = Color.White,
            disabledContainerColor = Ink.copy(alpha = 0.35f),
            disabledContentColor = Color.White.copy(alpha = 0.8f),
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun SecondaryPillButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    loading: Boolean,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (enabled) Hairline else Hairline.copy(alpha = 0.6f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Ink,
            disabledContainerColor = Color.White,
            disabledContentColor = MutedText,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(22.dp), color = Ink, strokeWidth = 2.dp)
        } else {
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun LoginScreenPreview() {
    DiJoTheme {
        LoginScreen(
            state = LoginUiState(email = "you@email.com", password = "Abcdefg"),
            onEmailChange = {},
            onPasswordChange = {},
            onLogInClick = {},
            onSignUpClick = {},
        )
    }
}
