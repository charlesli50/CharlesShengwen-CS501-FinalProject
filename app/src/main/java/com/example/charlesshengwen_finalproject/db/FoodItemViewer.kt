package com.example.charlesshengwen_finalproject.db

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun FoodItemViewer(
    viewModel: FoodViewModel = viewModel()
){
    val foodItems by viewModel.foodItems.collectAsState()
    val status by viewModel.status.collectAsState("")

    var foodName by remember { mutableStateOf(TextFieldValue("")) }
    var calories by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
        Date()
    )) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        FoodItemSelect(
            onAddFoodItem = { name, calories ->
                viewModel.addFoodItem(
                    FoodItem(
                        name = name,
                        calories = calories,
                        date = selectedDate
                    )
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = status, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        // Select Date Section
        Text(text = "Select Date", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { selectedDate = it },
            label = { Text("Date (yyyy-MM-dd)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.fetchFoodItemsByDate(selectedDate) }) {
            Text(text = "Fetch Food Items")
        }

        // Food Items List
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Food Items", style = MaterialTheme.typography.titleLarge)
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(foodItems) { item ->
                Text(text = "${item.name}: ${item.calories} calories on ${item.date}")
            }
        }
    }
}

@Composable
fun FoodItemSelect(
    onAddFoodItem: (String, Int) -> Unit,
    modifier: Modifier = Modifier
){
    var foodName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Food Item", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("Food Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val calorieCount = calories.toIntOrNull()
            if (calorieCount != null) {
                onAddFoodItem(foodName, calorieCount)
                foodName = ""
                calories = ""
            }
        }) {
            Text(text = "Add Food Item")
        }
    }
}