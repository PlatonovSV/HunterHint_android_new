package ru.openunity.hunterhint.ui.searchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.navigation.HuntTopAppBar
import ru.openunity.hunterhint.ui.booking.HuntingMethodItem
import ru.openunity.hunterhint.ui.booking.SelectDate
import ru.openunity.hunterhint.ui.booking.UserDate
import ru.openunity.hunterhint.ui.components.ComponentScreen
import ru.openunity.hunterhint.ui.enums.GuidingPreference
import ru.openunity.hunterhint.ui.enums.HuntingMethods
import ru.openunity.hunterhint.ui.enums.HuntingResources
import ru.openunity.hunterhint.ui.registration.Month
import ru.openunity.hunterhint.ui.registration.MonthDialog

@Composable
internal fun SearchFiltersRoute(
    navigateUp: () -> Unit,
    navigateToSearchScreen: (List<Int>) -> Unit,
    modifier: Modifier = Modifier, viewModel: SearchFiltersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    if (uiState.resourcesHint.allLocal.isEmpty()) {
        val allLocalResources = HuntingResources.entries.map {
            Pair(it.id, stringResource(id = it.stringRes))
        }

        val allLocalMethods = HuntingMethods.entries.map {
            Pair(it.id, stringResource(id = it.stringRes))
        }

        val allLocalRegions = Regions.entries.map {
            Pair(it.id, stringResource(id = it.stringResId))
        }
        viewModel.setAllLocal(allLocalResources, allLocalMethods, allLocalRegions)
    }
    Scaffold(
        topBar = {
            HuntTopAppBar(
                strResId = R.string.search_for_hunting_farms,
                navigateUp = navigateUp
            )
        },
        modifier = modifier
    ) { padding ->

        ComponentScreen(
            loadingStrResId = R.string.search,
            waitContent = {
                SearchFiltersScreen(
                    uiState, viewModel, it.padding(padding)
                )
            },
            successContent = {
                navigateToSearchScreen(uiState.groundIds)
            },
            retryOnErrorAction = viewModel::findGrounds,
            state = uiState.groundIdsState,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun SearchFiltersScreen(
    uiState: SearchFiltersUiState,
    viewModel: SearchFiltersViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(18.dp)
            .fillMaxSize()
    ) {
        item {
            ChoseDate(
                dismissDialogs = viewModel::dismissDialogs,
                isStartMonthDialogShow = uiState.isStartMonthDialogShow,
                isFinalMonthDialogShow = uiState.isFinalMonthDialogShow,
                isShowStartError = uiState.isShowStartError,
                isShowFinalError = uiState.isShowFinalError,
                updateStartMonth = viewModel::updateStartMonth,
                updateFinalMonth = viewModel::updateFinalMonth,
                showFinalMonthDialog = viewModel::showFinalMonthDialog,
                updateFinalDay = viewModel::updateFinalDay,
                updateFinalYear = viewModel::updateFinalYear,
                finalDay = uiState.finalDay,
                updateStartYear = viewModel::updateStartYear,
                updateStartDay = viewModel::updateStartDay,
                showStartMonthDialog = viewModel::showStartMonthDialog,
                startDay = uiState.startDay
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (uiState.minPrice == 0) "" else uiState.minPrice.toString(),
                    onValueChange = viewModel::updateMinPrice,
                    isError = uiState.resourcesHint.isError,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(
                            text = stringResource(id = R.string.min_price),
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp, 10.dp)
                        .background(color = Color.Green)
                )
                OutlinedTextField(
                    value = if (uiState.maxPrice == Int.MAX_VALUE) "" else uiState.maxPrice.toString(),
                    onValueChange = viewModel::updateMaxPrice,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(
                            text = stringResource(id = R.string.max_price),
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        }
        item {
            OutlinedTextField(
                value = uiState.resourcesHint.current.second,
                onValueChange = { viewModel.updateHint(it, SearchFiltersHints.RESOURCES) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.hunter_resource),
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )

        }
        items(uiState.resourcesHint.hints) {
            FiltersHint(
                hint = it.second,
                onClick = { viewModel.selectHint(it, SearchFiltersHints.RESOURCES) })
        }
        item {
            OutlinedTextField(
                value = uiState.methodHint.current.second,
                onValueChange = { viewModel.updateHint(it, SearchFiltersHints.METHOD) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.hunting_method),
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )
        }
        items(uiState.methodHint.hints) {
            FiltersHint(
                hint = it.second,
                onClick = { viewModel.selectHint(it, SearchFiltersHints.METHOD) })
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp)
            ) {
                OutlinedTextField(
                    value = uiState.huntersNumber.toString(),
                    onValueChange = viewModel::changeHuntersNumber,
                    isError = uiState.resourcesHint.isError,
                    modifier = Modifier
                        .weight(1f)
                        .height(86.dp),
                    label = {
                        Text(
                            text = stringResource(id = R.string.number_of_hunters),
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp, 10.dp)
                )
                OutlinedTextField(
                    value = if (uiState.guestsNumber > 0) uiState.guestsNumber.toString() else "",
                    onValueChange = viewModel::changeGuestsNumber,
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    label = {
                        Text(
                            text = stringResource(id = R.string.number_of_guests),
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        items(GuidingPreference.entries) {
            HuntingMethodItem(
                isSelect = uiState.guidingPreferenceId == it.id,
                onClick = { viewModel.setGuidingPreferenceId(it.id) },
                stringRes = it.stringResId
            )
        }
        item {
            HuntingMethodItem(
                isSelect = uiState.guidingPreferenceId < 0,
                onClick = { viewModel.setGuidingPreferenceId(-1) },
                stringRes = R.string.mo_matter
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = uiState.regionHint.current.second,
                onValueChange = { viewModel.updateHint(it, SearchFiltersHints.REGION) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.region),
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )
        }
        items(uiState.regionHint.hints) {
            FiltersHint(
                hint = it.second,
                onClick = { viewModel.selectHint(it, SearchFiltersHints.REGION) })
        }


        item {
            OutlinedTextField(
                value = uiState.districtHint.current.second,
                onValueChange = { viewModel.updateHint(it, SearchFiltersHints.DISTRICT) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.municipal_district),
                    )
                },
                enabled = uiState.districtHint.allLocal.isNotEmpty(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )
        }
        items(uiState.districtHint.hints) {
            FiltersHint(
                hint = it.second,
                onClick = { viewModel.selectHint(it, SearchFiltersHints.DISTRICT) })
        }

        item {
            OutlinedTextField(
                value = uiState.groundsNameHint.current.second,
                onValueChange = { viewModel.updateHint(it, SearchFiltersHints.GROUNDS_NAME) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.grounds_name),
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )
        }
        items(uiState.groundsNameHint.hints) {
            FiltersHint(
                hint = it.second,
                onClick = { viewModel.selectHint(it, SearchFiltersHints.GROUNDS_NAME) })
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(24.dp))
                HuntingMethodItem(
                    isSelect = uiState.isNeedsHotel,
                    onClick = viewModel::updateNeedsHotel,
                    stringRes = R.string.needs_hotel
                )
                HuntingMethodItem(
                    isSelect = uiState.isNeedsBath,
                    onClick = viewModel::updateNeedsBath,
                    stringRes = R.string.needs_bath
                )

            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(64.dp)
            ) {
                Button(onClick = viewModel::findGrounds) {
                    Text(
                        text = stringResource(id = R.string.search),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }

    }


}


@Composable
fun FiltersHint(
    hint: String, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(28.dp, 4.dp)
            .height(48.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = hint, modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ChoseDate(
    dismissDialogs: () -> Unit,
    isStartMonthDialogShow: Boolean,
    isFinalMonthDialogShow: Boolean,
    isShowStartError: Boolean,
    isShowFinalError: Boolean,
    updateStartMonth: (Month) -> Unit,
    updateFinalMonth: (Month) -> Unit,
    modifier: Modifier = Modifier,
    showFinalMonthDialog: () -> Unit,
    updateFinalDay: (String) -> Unit,
    updateFinalYear: (String) -> Unit,
    finalDay: UserDate,
    updateStartYear: (String) -> Unit,
    updateStartDay: (String) -> Unit,
    showStartMonthDialog: () -> Unit,
    startDay: UserDate
) {
    Column(
        modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.start_date), style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectDate(
            isShowError = isShowStartError,
            userDate = startDay,
            onMonthClick = showStartMonthDialog,
            onUserDayChanged = updateStartDay,
            onUserYearChanged = updateStartYear,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.leave_date), style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectDate(
            isShowError = isShowFinalError,
            userDate = finalDay,
            onMonthClick = showFinalMonthDialog,
            onUserDayChanged = updateFinalDay,
            onUserYearChanged = updateFinalYear,
            modifier = Modifier
        )
    }

    if (isStartMonthDialogShow) {
        MonthDialog(
            onDismissRequest = dismissDialogs, onSelect = updateStartMonth
        )
    }
    if (isFinalMonthDialogShow) {
        MonthDialog(
            onDismissRequest = dismissDialogs, onSelect = updateFinalMonth
        )
    }
}
