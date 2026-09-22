package edu.virginia.cs4720.ear6xt

import DetailScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.virginia.cs4720.bucketlist.ui.theme.BucketTheme

class DetailActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val id = intent.getStringExtra("ITEM_ID") ?: ""
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BucketTheme {
                Scaffold { innerPadding ->
                    DetailScreen(id = id, modifier = Modifier.padding(innerPadding), onDone = { finish() })
                }
            }
        }
    }
}
/*Detail Activity
• Pre-populated with the tapped item's data; adds a completion checkbox and displays the completed
date (not directly editable - it follows the checkbox, today's date on complete, null on un-complete)
• Same Save/Cancel semantics as Create*.*/

