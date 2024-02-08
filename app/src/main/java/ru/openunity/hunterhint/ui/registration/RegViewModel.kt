package ru.openunity.hunterhint.ui.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.openunity.hunterhint.data.GroundsRepository

class RegViewModel(private val repository: GroundsRepository) : ViewModel() {
    var userName by mutableStateOf("")
        private set

    var userLastName by mutableStateOf("")
        private set

    fun updateUserName(userInput: String){
        userName = userInput
    }

    fun updateUserLastName(userInput: String){
        userLastName = userInput
    }
}