package edu.virginia.cs4720.collegefootballscores

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Upsert
    suspend fun upsertAll(games: List<GameEntity>)

    @Query("SELECT * FROM games WHERE week = :week AND division = :division ORDER BY startDate, startTime")
    fun observeGames(week: Int, division: String): Flow<List<GameEntity>>

    @Query("SELECT DISTINCT week FROM games ORDER BY week")
    fun observeWeeks(): Flow<List<Int>>

    @Query("SELECT DISTINCT division FROM games ORDER BY division")
    fun observeDivisions(): Flow<List<String>>
}