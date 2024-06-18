package ru.openunity.hunterhint.ui.searchFilters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.dto.GroundsNameDto
import ru.openunity.hunterhint.ui.components.ComponentError
import ru.openunity.hunterhint.ui.components.ComponentSuccess
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
        val params = getParams()
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    groundIdsState = try {
                        _uiState.update {
                            it.copy(
                                groundIds = groundRepository.getGroundIds(params)
                            )
                        }
                        ComponentSuccess
                    } catch (e: IOException) {
                        ComponentError(R.string.server_error)
                    } catch (e: HttpException) {
                        ComponentError(R.string.no_internet)
                    }
                )
            }

        }
    }

    private fun getParams(): Map<String, String> {
        val params = mutableMapOf<String, String>()
        val state = uiState.value
        if (state.minPrice > 0) {
            params[SearchFilters.MIN_PRICE.id.toString()] = state.minPrice.toString()
        }
        if (state.maxPrice < Int.MAX_VALUE) {
            params[SearchFilters.MAX_PRICE.id.toString()] = state.maxPrice.toString()
        }
        if (state.startDay.date <= (state.finalDay.date) && state.startDay.date > LocalDateTime.now()) {
            params[SearchFilters.START_DATE.id.toString()] = state.startDay.date.toString()
            params[SearchFilters.FINAL_DATE.id.toString()] = state.finalDay.date.toString()
        }
        if (state.resourcesHint.current.first != -1) {
            params[SearchFilters.RESOURCES_TYPE.id.toString()] = state.resourcesHint.current.first.toString()
        }
        if (state.methodHint.current.first != -1) {
            params[SearchFilters.HUNTING_METHOD.id.toString()] = state.methodHint.current.first.toString()
        }
        if (state.huntersNumber > 1) {
            params[SearchFilters.NUMBER_HUNTERS.id.toString()] = state.huntersNumber.toString()
        }
        if (state.guestsNumber > 0) {
            params[SearchFilters.NUMBER_GUESTS.id.toString()] = state.guestsNumber.toString()
        }
        if (state.guidingPreferenceId != -1) {
            params[SearchFilters.SUPPORT.id.toString()] = state.guidingPreferenceId.toString()
        }
        if (state.regionHint.current.first != -1) {
            params[SearchFilters.REGION.id.toString()] = state.regionHint.current.first.toString()
        }
        if (state.districtHint.current.first != -1) {
            params[SearchFilters.MUNICIPAL_DISTRICT.id.toString()] = state.districtHint.current.first.toString()
        }
        if (state.groundsNameHint.current.first != -1) {
            params[SearchFilters.GROUNDS_NAME.id.toString()] = state.groundsNameHint.current.first.toString()
        }
        if (state.isNeedsHotel) {
            params[SearchFilters.IS_NEED_ACCOMMODATION.id.toString()] = ""
        }
        if (state.isNeedsBath) {

            params[SearchFilters.IS_NEED_BATHHOUSE.id.toString()] = ""
        }
        return params
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