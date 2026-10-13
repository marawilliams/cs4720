package edu.virginia.cs4720.collegefootballscores

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepository(private val dao: GameDao) {

    fun games(week: Int, division: String): Flow<List<Game>> =
        dao.observeGames(week, division).map { list -> list.map { it.toGame() } }

    suspend fun refresh(division: String, year: Int, week: Int) {
        val response = ApiClient.apiService.getGames(
            division = division,
            year = year,
            week = "%02d".format(week)
        )
        val entities = response.games.orEmpty()
            .mapNotNull { it.game }
            .filter { it.gameID.isNotBlank() }
            .map { it.toEntity(week, division) }
        dao.upsertAll(entities)
    }
}