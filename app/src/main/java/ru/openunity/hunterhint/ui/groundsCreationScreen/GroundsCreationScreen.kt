package ru.openunity.hunterhint.ui.groundsCreationScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.dto.NewGroundDto
import ru.openunity.hunterhint.navigation.HuntTopAppBar
import ru.openunity.hunterhint.ui.booking.HuntingMethodItem
import ru.openunity.hunterhint.ui.bottomAppBar.HuntBottomAppBar
import ru.openunity.hunterhint.ui.components.ComponentScreen
import ru.openunity.hunterhint.ui.components.ComponentState
import ru.openunity.hunterhint.ui.components.ComponentWait
import ru.openunity.hunterhint.ui.searchFilters.FiltersHint
import ru.openunity.hunterhint.ui.searchFilters.Regions
import ru.openunity.hunterhint.ui.searchFilters.SearchFiltersHints

@Composable
internal fun GroundsCreationRoute(
    navigateUp: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GroundsCreationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.regionHint.allLocal.isEmpty()) {

        val allLocalRegions = Regions.entries.map {
            Pair(it.id, stringResource(id = it.stringResId))
        }
        viewModel.setAllLocal(allLocalRegions)

    }

    Scaffold(topBar = {
        HuntTopAppBar(
            strResId = R.string.create_ground, navigateUp = navigateUp
        )
    }, bottomBar = { HuntBottomAppBar(navController = navController) }) { padding ->
        ComponentScreen(
            loadingStrResId = R.string.creating_review,
            waitContent = {
                GroundsCreationScreen(
                    newGroundDto = uiState.newGroundDto,
                    setImages = viewModel::setImages,
                    createGround = viewModel::createGround,
                    selectedImages = uiState.selectedImages,
                    onCommentChange = viewModel::changeComment,
                    removeImage = viewModel::removeImage,
                    imageSentState = uiState.imageUploading,
                    viewModel = viewModel,
                    uiState = uiState,
                    modifier = it.padding(padding)
                )
            },
            successContent = {
                navigateUp()
            },
            retryOnErrorAction = viewModel::uploadGround,
            state = uiState.groundsUploading,
            modifier = modifier.fillMaxSize()
        )
    }
}


@Composable
fun GroundsCreationScreen(
    viewModel: GroundsCreationViewModel,
    uiState: GroundsCreationUiState,
    newGroundDto: NewGroundDto,
    removeImage: (Uri) -> Unit,
    imageSentState: ComponentState,
    createGround: () -> Unit,
    selectedImages: List<Uri>,
    setImages: (List<Uri>) -> Unit,
    onCommentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            setImages(it)
        }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ComponentScreen(
            loadingMessage = stringResource(
                id = R.string.uploaded_out_of,
                newGroundDto.photoUrls.size,
                selectedImages.size
            ),
            waitContent = {
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(selectedImages) { uri ->
                        Image(painter = rememberAsyncImagePainter(uri),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(16.dp, 8.dp)
                                .size(100.dp)
                                .clickable {
                                    removeImage(uri)
                                })
                    }
                }
            },
            successContent = {},
            retryOnErrorAction = createGround,
            state = imageSentState,
            loadingStrResId = R.string.empty
        )
        OutlinedTextField(value = newGroundDto.groundsName,
            onValueChange = onCommentChange,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            enabled = imageSentState == ComponentWait,
            shape = RoundedCornerShape(42.dp),
            label = {
                Text(
                    text = stringResource(id = R.string.grounds_name),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            suffix = {
                ImageSelectionButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.wrapContentSize()
                )
            })
        GroundsParameters(viewModel = viewModel, uiState = uiState)


    }
}

@Composable
fun ImageSelectionButton(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.add_photo),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
    }
}

@Composable
fun GroundsParameters(
    viewModel: GroundsCreationViewModel,
    uiState: GroundsCreationUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(18.dp)
            .fillMaxSize()
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (uiState.newGroundDto.baseCoordinate.first() == .0) "" else uiState.newGroundDto.baseCoordinate.first()
                        .toString(),
                    onValueChange = viewModel::changeFirstBaseCoordinate,
                    isError = false,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(
                            text = stringResource(id = R.string.latitude),
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    )
                )
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp, 10.dp)
                        .background(color = Color.Green)
                )
                OutlinedTextField(
                    value = if (uiState.newGroundDto.baseCoordinate.last() == .0) "" else uiState.newGroundDto.baseCoordinate.last()
                        .toString(),
                    onValueChange = viewModel::changeLastBaseCoordinate,
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(
                            text = stringResource(id = R.string.longitude),
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            }
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
                    value = uiState.newGroundDto.maxNumberHunters.toString(),
                    onValueChange = viewModel::changeMaxNumberHunters,
                    isError = false,
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
                    value = if (uiState.newGroundDto.hotelCapacity > 0) uiState.newGroundDto.hotelCapacity.toString() else "",
                    onValueChange = viewModel::changeHotelCapacity,
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    label = {
                        Text(
                            text = stringResource(id = R.string.hotel_capacity),
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(24.dp))
                HuntingMethodItem(
                    isSelect = uiState.newGroundDto.informationHotel,
                    onClick = viewModel::updateThereIsHotel,
                    stringRes = R.string.there_is_a_hotel
                )
                HuntingMethodItem(
                    isSelect = uiState.newGroundDto.informationBath,
                    onClick = viewModel::updateThereIsBath,
                    stringRes = R.string.there_is_a_bath
                )

            }
        }

        if (uiState.newGroundDto.informationHotel) {
            item {
                OutlinedTextField(
                    value = if (uiState.newGroundDto.accommodationCost > 0) uiState.newGroundDto.accommodationCost.toString() else "",
                    onValueChange = viewModel::updateAccommodationCost,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(id = R.string.accommodation_cost, uiState.newGroundDto.accommodationCost),
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
                value = uiState.newGroundDto.companyName,
                onValueChange = viewModel::updateCompanyName,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.company_name),
                    )
                },
            )
        }

        item {
            OutlinedTextField(
                value = uiState.newGroundDto.groundsDescription,
                onValueChange = viewModel::updateGroundsDescription,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.grounds_description),
                    )
                },
            )
        }

        item {
            OutlinedTextField(
                value = if (uiState.newGroundDto.groundsArea > .0) uiState.newGroundDto.groundsArea.toString() else "",
                onValueChange = viewModel::updateGroundsArea,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.grounds_area, uiState.newGroundDto.groundsArea.toString()),
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal
                )
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(64.dp)
            ) {
                Button(onClick = viewModel::createGround) {
                    Text(
                        text = stringResource(id = R.string.create_ground),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }

    }
}
