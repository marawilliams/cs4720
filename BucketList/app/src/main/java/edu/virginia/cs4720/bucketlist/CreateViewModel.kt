package edu.virginia.cs4720.ear6xt

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class CreateViewModel : ViewModel() {

    var activity by mutableStateOf("")
        private set
    var dueDate by mutableStateOf("")
        private set

    fun updateActivity(input: String){
        activity = input
    }

    fun updateDate(input: String){
        dueDate = input
    }

    fun items() = BucketRepository.items
    @RequiresApi(Build.VERSION_CODES.O)
    fun toggle(id: String) {
        val item = BucketRepository.items.first { it.id == id}
        BucketRepository.update(
            if (item.done) item.copy(done = false, completedDate = null)
            else item.copy (done = true, completedDate = LocalDate.now())
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun save(){
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)
        val dateLocal = LocalDate.parse(dueDate, formatter)

        val item = BucketItem(name = activity, dueDate = dateLocal, id = Uuid.random().toString())
        BucketRepository.add(item)
    }

    fun canSave(): Boolean {
        return activity.isNotBlank() &&  dueDate.isNotBlank()
    }
}