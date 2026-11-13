package edu.virginia.cs4720.StepActivityLogger

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class Log(val date: LocalDate, val stepCount: Int = 0)

@RequiresApi(Build.VERSION_CODES.O)
fun Log.dateLabel(): String = date.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy"))
