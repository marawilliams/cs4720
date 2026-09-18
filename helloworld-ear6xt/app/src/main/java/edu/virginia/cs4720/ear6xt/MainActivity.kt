package edu.virginia.cs4720.ear6xt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.virginia.cs4720.ear6xt.ui.theme.Helloworldear6xtTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Helloworldear6xtTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Greeting()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    var name by remember {mutableStateOf("")}
    var salutation by remember {mutableStateOf("Hello")}

    if (name == ""){
        Text(
            text = "$salutation, Your name here!"
        )
    }
    else {
        Text(
            text = "$salutation, $name!"
        )
    }

    TextField( value = name, onValueChange = { name = it}, label = {Text("Name")})
    Row() {
        listOf("Hello", "Bonjour", "Ciao", "Ya Sou").forEach { word ->
            Button(onClick = { salutation = word }) { Text(word) }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Helloworldear6xtTheme {
        Column(){
            Greeting()
        }
    }
}