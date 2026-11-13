package edu.virginia.cs4720.StepActivityLogger

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface LogDao {
    @Upsert
    suspend fun upsertAll(games: List<LogEntity>)

    @Query("SELECT * FROM logs ORDER BY date")
    fun observeLogs(date: LocalDate, stepCount: Int): Flow<List<LogEntity>>

//    @Query("SELECT * FROM logs WHERE date = :date AND stepCount = :stepCount ORDER BY date")
//    fun observeLogs(date: Int, stepCount: Int): Flow<List<LogEntity>>


//    @Query("SELECT DISTINCT week FROM games ORDER BY week")
//    fun observeWeeks(): Flow<List<Int>>
//
//    @Query("SELECT DISTINCT division FROM games ORDER BY division")
//    fun observeDivisions(): Flow<List<String>>
}