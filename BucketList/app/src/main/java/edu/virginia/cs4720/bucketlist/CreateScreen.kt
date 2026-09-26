package edu.virginia.cs4720.ear6xt

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlin.comparisons.compareBy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import androidx.compose.ui.res.painterResource
import edu.virginia.cs4720.bucketlist.R

import java.util.Date
import java.util.Locale
import java.util.TimeZone

//goes inside listscreen(vm)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateScreen ( modifier: Modifier, vm: CreateViewModel = viewModel(), onDone: () -> Unit) {

    val sorted = vm.items().sortedWith(
        compareBy({ it.done }, { it.dueDate })
    )
    val context = LocalContext.current
    Column (modifier = modifier.fillMaxSize().padding(top = 50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        field(vm)
        DatePickerDocked(vm)
        Spacer(modifier = Modifier.weight(1f))
        Row(){
            Button (
                onClick = {onDone()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFd15654),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(10.dp).width(150.dp)
            ) { Text("Cancel")}
            Button (
                onClick = {vm.save(); onDone() },
                enabled = vm.canSave(),
                modifier = Modifier.padding(10.dp).width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContentColor = Color(0x5084732B),
                    disabledContainerColor = Color(0x5084732B)
                ),
            ) { Text("Save")}

        }



    }
}
//https://developer.android.com/develop/ui/compose/components/datepickers
// followed example above for creating date picker widget for project
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDocked(vm: CreateViewModel) {
    var showDatePicker by remember {mutableStateOf(false)}
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""


    Box (
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = {Text ("Due Date")},
            readOnly = true,
            leadingIcon = {
                IconButton(onClick = {showDatePicker = !showDatePicker }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_month),
                        contentDescription = "Select date",
                        tint = MaterialTheme.colorScheme.primary)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedTextColor = Color(0xFF84732B),
                focusedBorderColor = Color(0xFF84732B)
            )
            ,
            modifier = Modifier.fillMaxWidth().height(64.dp)
        )
        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false
                                      vm.updateDate(selectedDate)},
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().offset(y = 64.dp).shadow(elevation = 4.dp).background(MaterialTheme.colorScheme.surface).padding(16.dp)
                ){ DatePicker(state = datePickerState,  showModeToggle = false)}
            }
        }
    }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

@Composable
fun field(vm: CreateViewModel) {
    TextField( modifier = Modifier.fillMaxWidth(0.8f), value = vm.activity, onValueChange = { vm.updateActivity(it)
                                                      vm.canSave()}, label = {Text("Activity:")})
}

