package edu.virginia.cs4720.StepActivityLogger

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ScoreBoardResponse(
    val games: List<logWrapper>? = null
)

data class logWrapper(
    val log: Log? = null
)

data class Log(
    val date: LocalDate,
    val stepCount: Int = 0
)

enum class GameStatus { UPCOMING, LIVE, FINAL }

@RequiresApi(Build.VERSION_CODES.O)
fun Log.dateLabel(): String {
    val day = date.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy"))
    return "date - $day"
}