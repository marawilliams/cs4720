package edu.virginia.cs4720.ear6xt

import android.content.Intent
import android.os.Build
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import kotlin.jvm.java


//goes inside listscreen(vm)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun listscreen (vm: ListViewModel) {

    val sorted = vm.items().sortedWith(
        compareBy({ it.done }, { it.dueDate })
    )
    val context = LocalContext.current

    LazyColumn {
        items(sorted) { item ->
            BucketRow(
                name = item.name,
                dueDate = item.dueDate,
                done = item.done,
                onToggle = { vm.toggle(item.id) },
                onEdit = {
                    val i = Intent(context, DetailActivity::class.java)
                    i.putExtra("ITEM_ID", item.id)
                    context.startActivity(i)
                }
            )
        }
    }
}

@Composable
fun BucketRow(
    name: String,
    dueDate: LocalDate,
    done: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    Column(){
        Row(){
            Checkbox(checked = done, onCheckedChange = { onToggle()})
            Text("$name - $dueDate")
            Button(onClick = {onEdit()}){Text("✎")}
        }
    }
    TODO("Not yet implemented")
}






