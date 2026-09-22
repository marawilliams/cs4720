package edu.virginia.cs4720.ear6xt

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
    Column (modifier = modifier.fillMaxSize()) {
        field(vm)
        DatePickerDocked(vm)
        Button (
            onClick = {vm.save(); onDone() },
            enabled = vm.canSave()

        ) { Text("Save")}

        Button (
            onClick = {onDone()},

            ) { Text("Cancel")}


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
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = {Text ("Due Date")},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {showDatePicker = !showDatePicker }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_month),
                        contentDescription = "Select date",
                        tint = MaterialTheme.colorScheme.primary)
                }
            },
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
    TextField( value = vm.activity, onValueChange = { vm.updateActivity(it)
                                                      vm.canSave()}, label = {Text("Activity:")})
}

