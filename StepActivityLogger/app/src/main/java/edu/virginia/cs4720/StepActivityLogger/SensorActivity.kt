package edu.virginia.cs4720.StepActivityLogger

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import edu.virginia.cs4720.StepActivityLogger.ui.theme.SensoreScreenTheme
import android.Manifest
import android.provider.Settings

class SensorActivity : ComponentActivity() {
    private val viewModel: SensorViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            viewModel.stopSensors()
            viewModel.startSensors(granted)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SensoreScreenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold { inner ->
                        SensorScreen(
                            modifier = Modifier.padding(inner),
                            vm = viewModel,
                            onRequestPermission = {
                                permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                            },
                            onOpenSettings = {
                                startActivity(
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", packageName, null)
                                    )
                                )
                            },
                            onOpenHistory = { startActivity(Intent(this, HistoryActivity::class.java)) }
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        viewModel.startSensors(hasActivityPermission())   // re-checks, in case they changed it in Settings
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        viewModel.stopSensors()
    }

    private fun hasActivityPermission() =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) ==
                PackageManager.PERMISSION_GRANTED
}