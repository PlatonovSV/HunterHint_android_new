package ru.openunity.hunterhint.ui.bottomAppBar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.openunity.hunterhint.data.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class BottomAppBarViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

}