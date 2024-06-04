package ru.openunity.hunterhint.ui.searchFilters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.dto.GroundsNameDto
import ru.openunity.hunterhint.ui.registration.Month
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchFiltersViewModel @Inject constructor(
    private val groundRepository: GroundRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchFiltersUiState(
        groundsNameHint = Hint(
            key = SearchFiltersHints.GROUNDS_NAME,
            getAllLocal = { s ->
                if (s.length >= 3) {
                    viewModelScope.launch {
                        setAllLocalName(
                            groundRepository.getGroundsName(GroundsNameDto(-1, s)).map {
                                Pair(it.id, it.name)
                            })

                    }

                }
            }
        )
    ))

    val uiState = _uiState.asStateFlow()


    private fun setAllLocalName(pairs: List<Pair<Int, String>>) {
        _uiState.update {
            it.copy(
                groundsNameHint = it.groundsNameHint.copy(allLocal = pairs)
            )
        }
    }

    fun setAllLocal(
        allLocalResources: List<Pair<Int, String>>,
        allLocalMethods: List<Pair<Int, String>>,
        allLocalRegions: List<Pair<Int, String>>
    ) {
        _uiState.update {
            it.copy(
                resourcesHint = it.resourcesHint.copy(allLocal = allLocalResources),
                methodHint = it.methodHint.copy(allLocal = allLocalMethods),
                regionHint = it.regionHint.copy(allLocal = allLocalRegions),
            )
        }
    }

    fun selectHint(hint: Pair<Int, String>, key: SearchFiltersHints) {
        _uiState.update {
            it.copy(
                resourcesHint = it.resourcesHint.selectHint(hint, key),
                districtHint = it.districtHint.selectHint(hint, key),
                methodHint = it.methodHint.selectHint(hint, key),
                regionHint = it.regionHint.selectHint(hint, key),
                groundsNameHint = it.groundsNameHint.selectHint(hint, key),
            )
        }
        if (key == SearchFiltersHints.REGION) {
            viewModelScope.launch {
                try {
                    val dtoList = groundRepository.getDistricts(hint.first)
                    _uiState.update { state ->
                        state.copy(
                            districtHint = state.districtHint.copy(allLocal = dtoList.map {
                                Pair(it.id, it.name)
                            })
                        )
                    }
                } catch (_: IOException) {

                } catch (_: HttpException) {

                }
            }
        }
    }

    fun updateHint(userInput: String, key: SearchFiltersHints) {
        _uiState.update {
            it.copy(
                resourcesHint = it.resourcesHint.updateHints(userInput, key),
                districtHint = it.districtHint.updateHints(userInput, key),
                methodHint = it.methodHint.updateHints(userInput, key),
                regionHint = it.regionHint.updateHints(userInput, key),
                groundsNameHint = it.groundsNameHint.updateHints(userInput, key)
            )
        }
    }


    fun changeHuntersNumber(userInput: String) {
        val number = userInput.toIntOrNull()
        if (number != null && number >= 1 && number <= 99) {
            _uiState.update {
                it.copy(
                    huntersNumber = number
                )
            }
        }
    }

    fun changeGuestsNumber(userInput: String) {
        val number = userInput.toIntOrNull()
        if (number != null && number >= 0 && number <= 99) {
            _uiState.update {
                it.copy(
                    guestsNumber = number
                )
            }
        }
    }

    fun setGuidingPreferenceId(id: Int) {
        _uiState.update {
            it.copy(
                guidingPreferenceId = id
            )
        }
    }

    fun dismissDialogs() {
        _uiState.update {
            it.copy(isStartMonthDialogShow = false, isFinalMonthDialogShow = false)
        }
    }


    fun showStartMonthDialog() {
        _uiState.update {
            it.copy(
                isStartMonthDialogShow = true,
            )
        }
    }

    fun showFinalMonthDialog() {
        _uiState.update {
            it.copy(
                isFinalMonthDialogShow = true,
            )
        }
    }

    fun updateStartDay(userInput: String) {
        _uiState.update {
            it.copy(
                startDay = it.startDay.updateDay(userInput), isShowStartError = false
            )
        }
    }

    fun updateStartMonth(userInput: Month) {
        _uiState.update {
            it.copy(
                startDay = it.startDay.updateMonth(userInput), isShowStartError = false
            )
        }
        dismissDialogs()
    }

    fun updateStartYear(userInput: String) {
        _uiState.update {
            it.copy(
                startDay = it.startDay.updateYear(userInput), isShowStartError = false
            )
        }
    }


    fun updateFinalDay(userInput: String) {
        _uiState.update {
            it.copy(
                finalDay = it.finalDay.updateDay(userInput), isShowFinalError = false
            )
        }
    }

    fun updateFinalMonth(userInput: Month) {
        _uiState.update {
            it.copy(
                finalDay = it.finalDay.updateMonth(userInput), isShowFinalError = false
            )
        }
        dismissDialogs()
    }


    fun updateFinalYear(userInput: String) {
        _uiState.update {
            it.copy(
                finalDay = it.finalDay.updateYear(userInput), isShowFinalError = false
            )
        }
    }

    fun updateNeedsBath() {
        _uiState.update {
            it.copy(
                isNeedsBath = !it.isNeedsBath
            )
        }
    }

    fun updateNeedsHotel() {
        _uiState.update {
            it.copy(
                isNeedsHotel = !it.isNeedsHotel
            )
        }
    }


    fun findGrounds() {
        val parameters = mutableListOf<Pair<String, String>>()
        val state = uiState.value
        if (state.minPrice > 0) {
            parameters.add(Pair(SearchFilters.MIN_PRICE.name, state.minPrice.toString()))
        }
        if (state.maxPrice < Int.MAX_VALUE) {
            parameters.add(Pair(SearchFilters.MAX_PRICE.name, state.maxPrice.toString()))
        }
        if (state.startDay.date <= (state.finalDay.date)) {
            if (state.startDay.date.isAfter(LocalDateTime.now().plusHours(1))) {
                parameters.add(Pair(SearchFilters.START_DATE.name, state.startDay.date.toString()))
            }
            if (state.finalDay.date.isAfter(LocalDateTime.now().plusHours(1))) {
                parameters.add(Pair(SearchFilters.START_DATE.name, state.startDay.date.toString()))
            }
        }
        if (state.resourcesHint.current.first == Int.MAX_VALUE) {
            parameters.add(
                Pair(
                    SearchFilters.RESOURCES_TYPE.name,
                    state.resourcesHint.current.second
                )
            )
        }
        if (state.methodHint.current.first == Int.MAX_VALUE) {
            parameters.add(
                Pair(
                    SearchFilters.HUNTING_METHOD.name,
                    state.methodHint.current.second
                )
            )
        }
        if (state.huntersNumber > 1) {
            parameters.add(Pair(SearchFilters.NUMBER_HUNTERS.name, state.huntersNumber.toString()))
        }
        if (state.guestsNumber > 0) {
            parameters.add(Pair(SearchFilters.NUMBER_GUESTS.name, state.guestsNumber.toString()))
        }
    }

    fun updateMinPrice(userInput: String) {
        var price = userInput.toIntOrNull()
        if (userInput.isBlank()) price = 0
        if (price != null && price >= 0) {
            _uiState.update {
                it.copy(
                    minPrice = price
                )
            }
        }
    }

    fun updateMaxPrice(userInput: String) {
        var price = userInput.toIntOrNull()
        if (userInput.isBlank()) price = Int.MAX_VALUE
        if (price != null && price > 0) {
            _uiState.update {
                it.copy(
                    maxPrice = price
                )
            }
        }
    }
}