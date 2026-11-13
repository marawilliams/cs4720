package edu.virginia.cs4720.StepActivityLogger

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SensorScreen(
    modifier: Modifier = Modifier,
    vm: SensorViewModel = viewModel(),
    onRequestPermission: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenHistory: () -> Unit = {}
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Live Sensors", fontWeight = FontWeight.Bold, fontSize = 32.sp)

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Accelerometer", fontWeight = FontWeight.Bold)
                Text("x: %.2f".format(state.accelerometerX))
                Text("y: %.2f".format(state.accelerometerY))
                Text("z: %.2f".format(state.accelerometerZ))
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Steps today: ${state.steps}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    if (state.hardwareCounter) "Source: hardware step counter"
                    else "Source: accelerometer estimate"
                )
            }
        }

        if (!state.stepsPerm) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Step counting is off. Activity recognition permission lets Android " +
                                "share your step data with this app. Live accelerometer values still work. " +
                                "You can also turn it on later in Settings."
                    )
                    Row {
                        Button(onClick = onRequestPermission) { Text("Enable step counting") }
                        TextButton(onClick = onOpenSettings) { Text("Open Settings") }
                    }
                }
            }
        }

        Button(onClick = onOpenHistory) { Text("View history") }
    }
}