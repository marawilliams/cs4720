import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.virginia.cs4720.ear6xt.BucketRepository
import edu.virginia.cs4720.ear6xt.DetailViewModel
import edu.virginia.cs4720.ear6xt.ListViewModel

//package edu.virginia.cs4720.ear6xt
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreen (id: String, modifier: Modifier, vm: DetailViewModel = viewModel(), onDone: () -> Unit){
    vm.start(id)//first line
    Column(modifier = modifier) {
        Row() {
            Checkbox(checked = vm.done, onCheckedChange = { vm.toggle(id)})
            TextField(
                value = vm.activity,
                onValueChange = {
                    vm.activity = it
                },
                label = {
                    Text("Activity")
                }
            )
        }
        Text(
            text = "Due: ${vm.dueDate}"
        )
        if (vm.done){
            Text("Completed: ${vm.completed}")
        }

        Button(
            onClick = {
                vm.save(id)
                onDone() }
        ) {
            Text("Save")
        }

        Button(
            onClick = {
                onDone()
            }
        ) {
            Text("Cancel")
        }
    }

}
