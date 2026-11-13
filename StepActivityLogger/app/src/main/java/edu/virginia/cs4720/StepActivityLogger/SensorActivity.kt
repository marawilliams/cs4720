package edu.virginia.cs4720.StepActivityLogger

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import edu.virginia.cs4720.StepActivityLogger.ui.theme.SensoreScreenTheme

class SensorActivity : ComponentActivity() {
    private val viewModel: SensorViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SensoreScreenTheme {
                Scaffold { innerPadding -> HistoryScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}