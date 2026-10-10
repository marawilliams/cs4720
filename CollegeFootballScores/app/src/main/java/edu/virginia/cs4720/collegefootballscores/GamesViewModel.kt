package edu.virginia.cs4720.collegefootballscores

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

// need to get week range to determine week in season?

const val FIRST = 1
const val LAST = 14

@RequiresApi(Build.VERSION_CODES.O)
private fun currentSeasonWeek(): Int {
    val seasonStart = LocalDate.of(2026,9,3)
    val daysSince = ChronoUnit.DAYS.between(seasonStart, LocalDate.now())
    return((daysSince/7).toInt() + 1).coerceIn(FIRST, LAST)
}

enum class Division {
    FBS, FCS
}
@RequiresApi(Build.VERSION_CODES.O)
data class GamesUiState (
    val year: Int = 2026,
    val week: Int = currentSeasonWeek(),
    val division: Division = Division.FBS
)

@RequiresApi(Build.VERSION_CODES.O)
class GamesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()

    fun setWeek(week : Int) {
        _uiState.update { it.copy(week = week.coerceIn(FIRST, LAST))}
    }

    fun incWeek(){
        setWeek(_uiState.value.week + 1)
    }


    fun decWeek(){
        setWeek(_uiState.value.week - 1)
    }

    fun setDivision(div : Division) {
        _uiState.update { it.copy(division = div) }
    }
}

