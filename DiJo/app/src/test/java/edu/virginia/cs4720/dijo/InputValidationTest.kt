package edu.virginia.cs4720.dijo

import edu.virginia.cs4720.dijo.login.EmailValidator
import edu.virginia.cs4720.dijo.login.PasswordPolicy
import edu.virginia.cs4720.dijo.login.PasswordRule
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class InputValidationTest {

    @Test
    fun password_meeting_every_rule_is_valid() {
        assertTrue(PasswordPolicy.isValid("Abcdef1!"))
    }

    @Test
    fun password_too_short_fails_length_rule() {
        assertFalse(PasswordRule.MIN_LENGTH.isSatisfiedBy("Ab1!xyz"))
        assertTrue(PasswordRule.MIN_LENGTH.isSatisfiedBy("Ab1!xyzw"))
    }

    @Test
    fun missing_uppercase_fails() {
        assertFalse(PasswordRule.UPPERCASE.isSatisfiedBy("abcdef1!"))
        assertFalse(PasswordPolicy.isValid("abcdef1!"))
    }

    @Test
    fun missing_lowercase_fails() {
        assertFalse(PasswordRule.LOWERCASE.isSatisfiedBy("ABCDEF1!"))
        assertFalse(PasswordPolicy.isValid("ABCDEF1!"))
    }

    @Test
    fun missing_special_character_fails() {
        assertFalse(PasswordRule.SPECIAL_CHAR.isSatisfiedBy("Abcdefg1"))
        assertFalse(PasswordPolicy.isValid("Abcdefg1"))
    }

    @Test
    fun spaces_do_not_count_as_special_characters() {
        assertFalse(PasswordRule.SPECIAL_CHAR.isSatisfiedBy("Abcdef 1"))
    }

    @Test
    fun email_validation() {
        assertTrue(EmailValidator.isValid("you@email.com"))
        assertFalse(EmailValidator.isValid("you@email"))
        assertFalse(EmailValidator.isValid("you email@x.com"))
        assertFalse(EmailValidator.isValid(""))
    }
}