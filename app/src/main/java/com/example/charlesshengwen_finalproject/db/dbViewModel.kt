package com.example.charlesshengwen_finalproject.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.SharedFlow

data class FoodItem(
    val name: String = "",
    val calories: Int = 0,
    val date: String = ""
)

class FoodViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val foodCollection = db.collection("foodItems")

    private val _foodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodItems: StateFlow<List<FoodItem>> = _foodItems

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    private var listenerRegistration: ListenerRegistration? = null


    // Add food item to Firestore
    fun addFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            try {
                foodCollection.add(foodItem)
                _status.emit("Food item added successfully!")
            } catch (e: Exception) {
                _status.emit("Error adding food item: ${e.message}")
            }
        }
    }

    // Fetch food items by date
//    fun fetchFoodItemsByDate(date: String) {
//        viewModelScope.launch {
//            try {
//                val querySnapshot = foodCollection.whereEqualTo("date", date).get().await()
//                val items = querySnapshot.documents.mapNotNull { it.toObject(FoodItem::class.java) }
//                _foodItems.emit(items)
//            } catch (e: Exception) {
//                _status.emit("Error fetching food items: ${e.message}")
//            }
//        }
//    }

    fun fetchFoodItemsByDate(date: String) {
        // Remove previous listener if any
        listenerRegistration?.remove()

        listenerRegistration = foodCollection
            .whereEqualTo("date", date)
            .addSnapshotListener { snapshot, e ->
                viewModelScope.launch {
                    if (e != null) {
                        _status.emit("Error fetching food items: ${e.message}")
                        return@launch
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val items = snapshot.documents.mapNotNull { it.toObject(FoodItem::class.java) }
                        _foodItems.emit(items)
                    } else {
                        _foodItems.emit(emptyList())
                    }
                }
            }
    }

    // Cleanup listener when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}