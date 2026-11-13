package edu.virginia.cs4720.collegefootballscores

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.coroutines.cancellation.CancellationException

// need to get week range to determine week in season?

const val FIRST = 1
const val LAST = 15

@RequiresApi(Build.VERSION_CODES.O)
private fun currentSeasonWeek(): Int {
    val seasonStart = LocalDate.of(2026,9,3)
    val daysSince = ChronoUnit.DAYS.between(seasonStart, LocalDate.now())
    return((daysSince/7).toInt() + 1).coerceIn(FIRST, LAST)
}


@RequiresApi(Build.VERSION_CODES.O)
data class GamesUiState (
    val year: Int = 2026,
    val week: Int = currentSeasonWeek(),
    val division: String = "fbs",
    val isLoading: Boolean = false,
    val error: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
class GamesViewModel(app : Application) : AndroidViewModel(app) {

    private val repo = GameRepository(AppDatabase.get(app).gameDao())
    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games.asStateFlow()

    private var loadJob: Job? = null
    private var observeJob: Job? = null

    init {
        observeGames()
        loadGames() }

    private fun observeGames(){
        observeJob?.cancel()
        val s = _uiState.value
        observeJob = viewModelScope.launch {
            repo.games(s.week, s.division).collect { list ->
                _games.value = list
            }
        }
    }

    fun loadGames() {
        loadJob?.cancel()
        val s = _uiState.value
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null)}
            try {
                repo.refresh(s.division, s.year, s.week)
                _uiState.update { it.copy(isLoading = false)}
            } catch (e: CancellationException){
                throw e
            } catch (e: java.io.IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Oh no! There's no internet connection. Please check your connection and try again.",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Unable to load games. Please try again."
                    )
                }
            }
        }

    }

    fun setWeek(week : Int) {
        val clamped = week.coerceIn(FIRST, LAST)
        if (clamped == _uiState.value.week) return
        _uiState.update { it.copy(week=clamped)}
        observeGames()
        loadGames()
    }

    fun incWeek() = setWeek(_uiState.value.week + 1)
    fun decWeek() = setWeek(_uiState.value.week - 1)

    fun setDivision(div : String) {
        if (div == _uiState.value.division) return
        _uiState.update { it.copy(division = div) }
        observeGames()
        loadGames()
    }

    fun getDivision() : String = _uiState.value.division
}

