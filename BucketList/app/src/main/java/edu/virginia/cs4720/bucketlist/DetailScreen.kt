import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        Row() {

            Button(
                onClick = {
                    onDone()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFd15654),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(10.dp).width(150.dp)
            ) {
                Text("Cancel")
            }

            Button(
                    onClick = {
                        vm.save(id)
                        onDone()
                    },
                modifier = Modifier.padding(10.dp).width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContentColor = Color(0x5084732B),
                    disabledContainerColor = Color(0x5084732B)
                )) {
                Text("Save")
            }
        }
    }

}
