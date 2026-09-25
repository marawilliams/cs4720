package edu.virginia.cs4720.ear6xt

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import kotlin.comparisons.compareBy
import kotlin.jvm.java
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


//goes inside listscreen(vm)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen (modifier: Modifier, vm: ListViewModel = viewModel()) {

    val sorted = vm.items().sortedWith(
        compareBy({ it.done }, { it.dueDate })
    )
    println("sorted list" + sorted)

    val context = LocalContext.current
    Column (modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "to do items:",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier= modifier.fillMaxWidth())

        Box(Modifier.align(Alignment.CenterHorizontally).background(Color(0x8884732B))){
            if (sorted.size == 0){
                Text(text = "no activities added... ", color = Color.White)
            }
        }
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
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                context.startActivity(Intent(context, CreateActivity::class.java))
            }
        ){
            Text("Add Activity")
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
}






