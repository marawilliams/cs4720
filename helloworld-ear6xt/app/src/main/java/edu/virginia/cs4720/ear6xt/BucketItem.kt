package edu.virginia.cs4720.ear6xt

import android.widget.DatePicker
import java.time.LocalDate

//data class file
class BucketItem (
    val name: String = "",
    val dueDate: LocalDate? = null,
    val done: Boolean = false,
    val date: LocalDate = LocalDate.now()
    )