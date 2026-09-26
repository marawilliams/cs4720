package edu.virginia.cs4720.ear6xt

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
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

        Box(Modifier.align(Alignment.CenterHorizontally).background(Color(0x5084732B)).fillMaxWidth(0.8f).padding(15.dp).heightIn(min = 50.dp)){
            if (sorted.size == 0){
                Text(text = "no activities added... ", color = Color.White)
            }

            var row = 0
            LazyColumn () {
                items(sorted) { item ->
                    row = row % 2
                    if (row == 0){
                        Box(){
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
                    else{
                        Box(modifier.background(Color(0x5084732B))){
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

                    row++
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 10.dp), shape = RoundedCornerShape(2.dp),
            onClick = {
                context.startActivity(Intent(context, CreateActivity::class.java))
            }
        ){
            Text(text = "Add Activity", fontSize = 20.sp)
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
        Row(verticalAlignment = Alignment.CenterVertically){
            Checkbox( checked = done, onCheckedChange = { onToggle()}, colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color.White, checkmarkColor = Color(0xFF1C3A13)))
            Text(text = "$name - $dueDate", color = Color.White, modifier = Modifier.padding(10.dp))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {onEdit()}){Text(color = Color.White, text ="✎", fontSize =  25.sp )}
        }

    }
}






