package edu.virginia.cs4720.StepActivityLogger

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class LogRepository(private val dao: LogDao) {
    fun history(): Flow<List<Log>> =
        dao.observeAll().map { list -> list.map {it.toLog()}}

    suspend fun todaySteps(date: LocalDate): Int = dao.getByDate(date.toEpochDay())?.stepCount ?: 0

    suspend fun saveSteps(date: LocalDate, steps:Int) =
        dao.upsert(LogEntity(date.toEpochDay(), steps))
}