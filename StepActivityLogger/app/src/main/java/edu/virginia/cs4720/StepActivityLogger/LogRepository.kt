package edu.virginia.cs4720.StepActivityLogger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class LogRepository(private val dao: LogDao) {

    fun logs(date: LocalDate, stepCount: Int): Flow<List<Log>> =
        dao.observeLogs(date, stepCount).map { list -> list.map { it.toLog() } }

    suspend fun refresh(date: LocalDate, stepCount: Int) {
    }
}