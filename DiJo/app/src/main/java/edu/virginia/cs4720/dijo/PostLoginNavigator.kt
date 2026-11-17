package edu.virginia.cs4720.dijo.login

import android.app.Activity
import android.content.Intent
import edu.virginia.cs4720.dijo.MainActivity

/**
 * Helper file: the one place that decides where a successful login goes.
 *
 * The screen and ViewModel never mention a destination. When the home dashboard has its own
 * Activity, change [DefaultPostLoginNavigator] (or pass a different implementation from
 * LoginActivity) and nothing else in the login package changes.
 */
fun interface PostLoginNavigator {
    fun openHome(activity: Activity)
}

object DefaultPostLoginNavigator : PostLoginNavigator {
    override fun openHome(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java).apply {
            // Clear the back stack so Back from home does not return to the login screen.
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        activity.startActivity(intent)
        activity.finish()
    }
}
