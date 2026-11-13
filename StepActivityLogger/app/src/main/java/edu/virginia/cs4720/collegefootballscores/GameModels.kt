package edu.virginia.cs4720.collegefootballscores

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ScoreBoardResponse(
    val games: List<GameWrapper>? = null
)

data class GameWrapper(
    val game: Game? = null
)

data class Game(
    val gameID: String = "",
    val gameState: String = "",
    val startTime: String = "",
    val startDate: String = "",
    val currentPeriod: String = "",
    val contestClock: String = "",
    val finalMessage: String = "",
    val away: GameTeam = GameTeam(),
    val home: GameTeam = GameTeam()
)

data class TeamNames(
    val char6: String? = null,
    val short: String? = null,
    val full: String? = null
)

data class GameTeam(
    val score: String? = null,
    val rank: String? = null,
    val winner: Boolean = false,
    val names: TeamNames? = null
) {
    val displayName: String
        get() = names?.short ?: names?.full ?: names?.char6 ?: ""
}

enum class GameStatus { UPCOMING, LIVE, FINAL }

fun Game.status(): GameStatus = when {
    gameState.equals("final", true) -> GameStatus.FINAL
    gameState.equals("live", true) -> GameStatus.LIVE
    else -> GameStatus.UPCOMING
}

@RequiresApi(Build.VERSION_CODES.O)
fun Game.startLabel(): String {
    val day = runCatching {
        LocalDate.parse(startDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            .format(DateTimeFormatter.ofPattern("EEE, MMM d"))
    }.getOrDefault(startDate)
    return "$day - $startTime"
}

fun Game.periodLabel(): String = when (currentPeriod.lowercase()) {
    "1st" -> "Q1"
    "2nd" -> "Q2"
    "3rd" -> "Q3"
    "4th" -> "Q4"
    else -> currentPeriod
}

fun Game.finalLabel(): String {
    val raw = listOf(finalMessage, currentPeriod).firstOrNull { it.contains("OT", true) }
        ?: finalMessage.ifBlank { "FINAL" }
    return raw.replaceFirst("FINAL", "Final", ignoreCase = true)
}
