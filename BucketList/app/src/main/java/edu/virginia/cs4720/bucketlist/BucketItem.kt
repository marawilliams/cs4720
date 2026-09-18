package edu.virginia.cs4720.ear6xt

import android.widget.DatePicker
import java.time.LocalDate

//data class file
data class BucketItem (
    val name: String = "",
    val dueDate: LocalDate,
    val done: Boolean = false,
    val completedDate: LocalDate? = null,
    val id: String
    )