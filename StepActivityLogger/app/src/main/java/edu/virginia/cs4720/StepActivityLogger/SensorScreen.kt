package edu.virginia.cs4720.StepActivityLogger

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SensorScreen(modifier: Modifier = Modifier, vm: SensorViewModel = viewModel()){
    val state by vm.uiState.collectAsState()
    val logs by vm.logs.collectAsState()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Step History",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        when {
            state.isLoading -> item {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }
            state.error != null && logs.isEmpty() -> item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = state.error ?: "Unable to connect",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Button(onClick = { vm.loadLogs() }, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) { Text("Try Again") }
                }
            }
            logs.isEmpty() -> item {
                Text(
                    "Sorry! There are no logs yet!!",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                items(logs, key = { it.date }) { log -> LogCard(log) }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogCard(log: Log) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp)){
            Text(
                text = log.dateLabel(),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text (
                text = "steps: ${log.stepCount}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}