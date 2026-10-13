package edu.virginia.cs4720.collegefootballscores

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
import edu.virginia.cs4720.collegefootballscores.ui.theme.CollegeFootballScoresTheme

class GamesActivity : ComponentActivity() {
    private val viewModel: GamesViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollegeFootballScoresTheme {
                Scaffold { innerPadding -> GamesScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}