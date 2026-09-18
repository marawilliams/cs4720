package edu.virginia.cs4720.ear6xt

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class ListViewModel : ViewModel() {
    fun items() = BucketRepository.items
    @RequiresApi(Build.VERSION_CODES.O)
    fun toggle(id: String) {
        val item = BucketRepository.items.first { it.id == id}
        BucketRepository.update(
            if (item.done) item.copy(done = false, completedDate = null)
            else item.copy (done = true, completedDate = LocalDate.now())
        )
    }
}

