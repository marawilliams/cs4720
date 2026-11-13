package edu.virginia.cs4720.collegefootballscores

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val gameID: String,
    val week: Int,
    val division: String,
    val gameState: String,
    val startTime: String,
    val startDate: String,
    val currentPeriod: String,
    val contestClock: String,
    val finalMessage: String,

    val awayScore: String?,
    val awayRank: String?,
    val awayWinner: Boolean,
    val awayShort: String?,
    val awayFull: String?,
    val awayChar6: String?,

    val homeScore: String?,
    val homeRank: String?,
    val homeWinner: Boolean,
    val homeShort: String?,
    val homeFull: String?,
    val homeChar6: String?
)

fun Game.toEntity(week: Int, division: String) = GameEntity(
    gameID = gameID, week = week, division = division,
    gameState = gameState, startTime = startTime, startDate = startDate,
    currentPeriod = currentPeriod, contestClock = contestClock, finalMessage = finalMessage,
    awayScore = away.score, awayRank = away.rank, awayWinner = away.winner,
    awayShort = away.names?.short, awayFull = away.names?.full, awayChar6 = away.names?.char6,
    homeScore = home.score, homeRank = home.rank, homeWinner = home.winner,
    homeShort = home.names?.short, homeFull = home.names?.full, homeChar6 = home.names?.char6
)

fun GameEntity.toGame() = Game(
    gameID = gameID, gameState = gameState, startTime = startTime, startDate = startDate,
    currentPeriod = currentPeriod, contestClock = contestClock, finalMessage = finalMessage,
    away = GameTeam(awayScore, awayRank, awayWinner, TeamNames(awayChar6, awayShort, awayFull)),
    home = GameTeam(homeScore, homeRank, homeWinner, TeamNames(homeChar6, homeShort, homeFull))
)