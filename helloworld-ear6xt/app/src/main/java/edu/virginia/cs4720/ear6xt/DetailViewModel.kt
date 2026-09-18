package edu.virginia.cs4720.ear6xt

import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {
    private var loadedId: String? = null

    fun start(id:String) {
        if (loadedId != null) return
        val item = BucketRepository.items.first {it.id == id}
        loadedId = id
        //copy item fields into form state
    }

}
