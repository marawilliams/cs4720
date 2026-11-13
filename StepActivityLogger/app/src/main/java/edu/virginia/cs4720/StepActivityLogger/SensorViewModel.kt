package edu.virginia.cs4720.StepActivityLogger

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.coroutines.cancellation.CancellationException

@RequiresApi(Build.VERSION_CODES.O)
data class LogUiState (
    val date: LocalDate = LocalDate.now(),
    val stepCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
class SensorViewModel(app : Application) : AndroidViewModel(app) {

    private val repo = LogRepository(LogDatabase.get(app).logDAO())
    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    private val _logs = MutableStateFlow<List<Log>>(emptyList())
    val logs: StateFlow<List<Log>> = _logs.asStateFlow()

    private var loadJob: Job? = null
    private var observeJob: Job? = null

    init {
        observeLogs()
        loadLogs() }

    private fun observeLogs(){
        observeJob?.cancel()
        val s = _uiState.value
        observeJob = viewModelScope.launch {
            repo.logs(s.date, s.stepCount).collect { list ->
                _logs.value = list
            }
        }
    }

    fun loadLogs() {
        loadJob?.cancel()
        val s = _uiState.value
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null)}
            try {
                repo.refresh(s.date,s.stepCount)
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
}

