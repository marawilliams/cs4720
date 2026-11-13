package edu.virginia.cs4720.collegefootballscores

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GamesScreen(modifier: Modifier = Modifier, vm: GamesViewModel = viewModel()){
    val state by vm.uiState.collectAsState()
    val games by vm.games.collectAsState()
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
                        text = "College Games!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IconButton(
                        onClick = { vm.loadGames() },
                        modifier = Modifier.align(Alignment.CenterEnd).clip(RoundedCornerShape(12.dp)).background(Color.DarkGray)
                    ) {
                        Text("↻", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Text(
                    text = "Choose a week to view",
                    fontSize = 25.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                WeekSelector(state.week, { vm.decWeek() }, { vm.incWeek() })
                Text(
                    text = "Choose a division",
                    fontSize = 25.sp,
                    modifier = Modifier.fillMaxWidth(),

                )
                SimpleRadioButtonComponent(vm)
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
            state.error != null && games.isEmpty() -> item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = state.error ?: "Unable to connect",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Button(onClick = { vm.loadGames() }, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) { Text("Try Again") }
                }
            }
            games.isEmpty() -> item {
                Text(
                    "Sorry! There are no games this week!",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                items(games, key = { it.gameID }) { game -> GameCard(game) }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun weekDateRange(week: Int, year: Int = 2026): String {
    val week1Sunday = LocalDate.of(year, 8, 30)
    val sunday = week1Sunday.plusWeeks((week - 1).toLong())
    val saturday = sunday.plusDays(6)
    val formatter = DateTimeFormatter.ofPattern("MMM d")
    return "${sunday.format(formatter)} - ${saturday.format(formatter)}"
}

@RequiresApi(Build.VERSION_CODES.O)
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
        Button(onClick = onPrev, enabled = week > FIRST, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
            Text(text = "<", fontSize = 20.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Week $week", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(weekDateRange(week), fontSize = 14.sp, color = Color.Gray)
        }
        Button(onClick = onNext, enabled = week < LAST, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
            Text(text = ">", fontSize = 20.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SimpleRadioButtonComponent(vm: GamesViewModel ) {
    val radioOptions = listOf("fbs", "fcs")
    val selected = vm.uiState.collectAsState().value.division
    Row( modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == selected),
                        onClick = { vm.setDivision(text)},
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selected),
                    onClick = null,
                    modifier = Modifier.padding(8.dp),
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        Color.DarkGray,
                        Color.DarkGray
                    ),
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GameCard( game: Game) {
    val status = game.status()
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            TeamRow(game.away, "Away", status != GameStatus.UPCOMING, status == GameStatus.FINAL)
            TeamRow(game.home, "Home", status != GameStatus.UPCOMING, status == GameStatus.FINAL)
            Text(
                text = when (status) {
                    GameStatus.UPCOMING -> game.startLabel()
                    GameStatus.LIVE ->
                        if (game.contestClock.isBlank()) game.periodLabel()
                        else "${game.periodLabel()} - ${game.contestClock}"

                    GameStatus.FINAL -> game.finalLabel()
                },
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun TeamRow(team: GameTeam, role: String, showScore: Boolean, showWinner: Boolean) {
    val isWinner = showWinner && team.winner
    val weight = if (isWinner) FontWeight.Bold else FontWeight.Normal
    val rankText = if (!team.rank.isNullOrBlank()) "#${team.rank} " else ""
    val teamName = team.displayName
    val scoreText = team.score ?: ""
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(role.uppercase(), fontSize = 11.sp, modifier = Modifier.width(48.dp))
        Text(
            text = rankText + teamName + if (isWinner) " ✓" else "",
            fontWeight = weight,
            modifier = Modifier.weight(1f)
        )
        if (showScore) Text(scoreText, fontWeight = weight)
    }
}