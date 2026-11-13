package edu.virginia.cs4720.StepActivityLogger

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey val date: LocalDate,
    val stepCount: Int = 0
)

fun Log.toEntity(week: Int, division: String) = LogEntity(
    date = date, stepCount = stepCount
)

fun LogEntity.toLog() = Log(
    date = date, stepCount = stepCount
)