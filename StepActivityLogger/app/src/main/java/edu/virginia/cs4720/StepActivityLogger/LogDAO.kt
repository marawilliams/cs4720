package edu.virginia.cs4720.StepActivityLogger

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface LogDao {
    @Upsert
    suspend fun upsert(log: LogEntity)

    @Query("SELECT * FROM logs ORDER BY day DESC")
    fun observeAll(): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE day = :day ORDER BY day DESC")
    suspend fun getByDate(day: Long): LogEntity?
}