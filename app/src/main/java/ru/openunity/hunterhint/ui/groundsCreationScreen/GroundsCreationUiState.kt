package ru.openunity.hunterhint.ui.groundsCreationScreen

import android.net.Uri
import ru.openunity.hunterhint.dto.NewGroundDto
import ru.openunity.hunterhint.ui.components.ComponentState
import ru.openunity.hunterhint.ui.components.ComponentWait
import ru.openunity.hunterhint.ui.searchFilters.Hint
import ru.openunity.hunterhint.ui.searchFilters.SearchFiltersHints

data class GroundsCreationUiState(
    val selectedImages: List<Uri> = listOf(),
    val imageUploading: ComponentState = ComponentWait,
    val groundsUploading: ComponentState = ComponentWait,

    val regionHint: Hint = Hint(key = SearchFiltersHints.REGION),
    val districtHint: Hint = Hint(key = SearchFiltersHints.DISTRICT),

    val newGroundDto: NewGroundDto = NewGroundDto()
)