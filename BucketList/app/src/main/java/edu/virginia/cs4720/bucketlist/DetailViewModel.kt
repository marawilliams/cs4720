package edu.virginia.cs4720.ear6xt

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class DetailViewModel : ViewModel() {
    private var loadedId: String? = null

    var activity by mutableStateOf("")
    var dueDate by mutableStateOf(LocalDate.now())
    var completed by mutableStateOf<LocalDate?>(null)
    var done by mutableStateOf(false)

    fun start(id:String) {
        if (loadedId != null) return
        val item = BucketRepository.items.first {it.id == id}
        loadedId = id

        activity = item.name
        dueDate = item.dueDate
        completed = item.completedDate
        done = item.done
        //copy item fields into form state
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggle(id: String) {
        val item = BucketRepository.items.first { it.id == id}
        if (item.done){
            done = false
            completed = null
        }
        else {
            done = true
            completed = LocalDate.now()
        }
    }

    fun save(id: String) {

        val item = BucketRepository.items
            .first { it.id == id }

        val updatedItem = item.copy(
            name = activity,
            dueDate = dueDate,
            done = done,
            completedDate = completed
        )

        BucketRepository.update(updatedItem)
    }
}
