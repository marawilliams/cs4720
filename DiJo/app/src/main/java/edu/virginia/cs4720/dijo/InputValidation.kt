package edu.virginia.cs4720.dijo.login

/**
 * Helper file: pure Kotlin, no Android or Firebase imports, so it is trivially unit-testable.
 *
 * Each rule knows its own on-screen label and how to test a password. The screen simply loops
 * over [PasswordRule.entries], so adding or changing a rule needs no UI changes.
 */
enum class PasswordRule(val label: String) {
    MIN_LENGTH("At least ${PasswordPolicy.MIN_LENGTH} characters") {
        override fun isSatisfiedBy(password: String) = password.length >= PasswordPolicy.MIN_LENGTH
    },
    UPPERCASE("One uppercase letter") {
        override fun isSatisfiedBy(password: String) = password.any { it.isUpperCase() }
    },
    LOWERCASE("One lowercase letter") {
        override fun isSatisfiedBy(password: String) = password.any { it.isLowerCase() }
    },
    SPECIAL_CHAR("One special character") {
        // Anything that is not a letter, digit, or whitespace (for example ! @ # $ % ?).
        override fun isSatisfiedBy(password: String) =
            password.any { !it.isLetterOrDigit() && !it.isWhitespace() }
    };

    abstract fun isSatisfiedBy(password: String): Boolean
}

object PasswordPolicy {
    const val MIN_LENGTH = 8

    fun isValid(password: String): Boolean = PasswordRule.entries.all { it.isSatisfiedBy(password) }
}

object EmailValidator {
    private val pattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    fun isValid(email: String): Boolean = pattern.matches(email)
}
