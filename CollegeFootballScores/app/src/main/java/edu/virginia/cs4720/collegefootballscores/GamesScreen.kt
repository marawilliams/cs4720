package edu.virginia.cs4720.collegefootballscores

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GamesScreen(modifier: Modifier, vm: GamesViewModel = viewModel()){
    val state by vm.uiState.collectAsState()
    val radioOptions = listOf("FBS", "FCS")
    Column (modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "College Games!",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = "Choose a week to view",
            fontSize = 25.sp,
            modifier = modifier.fillMaxWidth()
        )
        //add selector
        WeekSelector(state.week, { vm.decWeek() }, { vm.incWeek() })
        Text(
            text = "Choose a division",
            fontSize = 25.sp,
            modifier = modifier.fillMaxWidth()
        )
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "fbs",
                fontSize = 20.sp,
                modifier = modifier.fillMaxWidth()
            )
            //toggle to choose between the two
            Text(
                text = "fcs",
                fontSize = 20.sp,
                modifier = modifier.fillMaxWidth()
            )
        }

        //where the games need to show up

    }
}

@Composable
fun WeekSelector(
    week: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onPrev, enabled = week > FIRST) {
            Text("<")
        }
        Text("Week $week")
        Button(onClick = onNext, enabled = week < LAST) {
            Text(">")
        }
    }
}