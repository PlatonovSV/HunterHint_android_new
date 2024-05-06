package ru.openunity.hunterhint.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.openunity.hunterhint.R

val AbrilFatface = FontFamily(
    Font(R.font.abril_fatface_regular)
)

val Montserrat = FontFamily(
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val Typography = Typography(
    /*displayLarge = TypographyTokens.DisplayLarge.copy(
        fontFamily = AbrilFatface,
        fontWeight = FontWeight.Normal,
    ),
    displayMedium = TextStyle(
        fontFamily = AbrilFatface,
        fontWeight = FontWeight.Bold,
    ),
    displaySmall = TextStyle(
        fontFamily = AbrilFatface,
        fontWeight = FontWeight.Bold,
    ),
    labelSmall = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
    ),

    bodyLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
    )*/
)
