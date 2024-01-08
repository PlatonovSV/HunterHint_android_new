package ru.openunity.hunterhint.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.components.GroundImages

@Composable
fun GroundsPage(
    changeImage: (Boolean) -> Unit,
    groundsPageUiState: GroundsPageUiState,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        GroundImages(
            images = groundsPageUiState.ground.images,
            changeImage = changeImage,
            numberOfCurrentImage = groundsPageUiState.numberOfCurrentImage,
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            ContentScale.Fit
        )
        Spacer(modifier = Modifier.size(50.dp))
        Text(text = groundsPageUiState.ground.toString())
    }
}


//description

//comment

//share button

@Composable
fun GroundsPageTitle(
    onClickSearch: () -> Unit, onClickShare: () -> Unit,
    onClickToFavorites: () -> Unit, modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Row(modifier = Modifier.weight(1.0f)
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.small
            ),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_filter),
                Modifier.padding(5.dp), tint = MaterialTheme.colorScheme.tertiary)
            Text(text = stringResource(R.string.find_in_hunter_hint),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 16.sp)
        }
        Icon(painter = painterResource(id = R.drawable.ios_share), contentDescription = stringResource(
            id = R.string.share
        ),modifier = Modifier.padding(8.dp,6.dp))
        Icon(painter = painterResource(id = R.drawable.favorite), contentDescription = stringResource(
            id = R.string.to_favorite
        ),modifier = Modifier.padding(0.dp,4.dp,8.dp,4.dp))
    }
}
