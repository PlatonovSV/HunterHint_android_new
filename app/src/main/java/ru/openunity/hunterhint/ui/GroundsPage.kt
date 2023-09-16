package ru.openunity.hunterhint.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import ru.openunity.hunterhint.ui.components.GroundImages

@Composable
fun GroundsPage(
    changeImage: (Boolean) -> Unit,
    groundsUiState: GroundsUiState,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .size(260.dp, 2.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
            )
            Spacer(
                modifier = Modifier
                    .size(16.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
        }
        GroundImages(
            images = groundsUiState.images,
            changeImage = changeImage,
            numberOfCurrentImage = groundsUiState.numberOfCurrentImage,
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            ContentScale.Inside
        )
        Spacer(modifier = Modifier.size(50.dp))
        Text(text = groundsUiState.ground.toString())
    }
}


//description

//comment

//share button
