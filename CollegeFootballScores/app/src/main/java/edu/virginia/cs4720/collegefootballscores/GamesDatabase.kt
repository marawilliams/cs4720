//package edu.virginia.cs4720.collegefootballscores
//
//import androidx.room3.ColumnInfo
//import androidx.room3.Dao
//import androidx.room3.Entity
//import androidx.room3.PrimaryKey
//import androidx.room3.Query
//
//@Entity
//data class Game {
//    @PrimaryKey val uid: Int,
//    @ColumnInfo(name = "home_team") val homeTeam: String,
//    @ColumnInfo(name = "away_team") val awayTeam: String,
//    @ColumnInfo("StartTime") val startTime: String,
//    @ColumnInfo("StartDate") val startDate: String,
//    @ColumnInfo("Home Score") val homeScore: Int?,
//    @ColumnInfo("Away Score") val awayScore: Int?,
//    @ColumnInfo("Time Remaining") val timeRemaining: Int?,
//    @ColumnInfo("Quarter") val quarter: Int?,
//    @ColumnInfo("Status") val status: String,
//    @ColumnInfo("Winner") val winner: String,
//}
//
//@Dao
//interface GameDao {
//    @Query("SELECT * FROM game")
//    suspend fun getAll(): List<Game>
//}