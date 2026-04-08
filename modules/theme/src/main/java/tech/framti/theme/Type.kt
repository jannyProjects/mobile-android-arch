package tech.framti.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import tech.framti.theme.R
import tech.framti.theme.colorMain

val openSansFamily = FontFamily(
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_bold, FontWeight.Bold),
)

@Immutable
data class CamlCustomTypography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val paragraph: TextStyle,
    val buttonText: TextStyle,
    val field: TextStyle,
    val fieldLabel: TextStyle,
    val fieldTextNormal: TextStyle,
    val fieldInactive: TextStyle,
    val hint: TextStyle,
    val left: TextStyle,
    val time: TextStyle,
    val small: TextStyle,
    val bold: TextStyle,
)

fun TextStyle.toSpannable(color: Color) {
    SpanStyle(
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily
    )
}

val CamlTypography = staticCompositionLocalOf {
    CamlCustomTypography(
        h1 = TextStyle( // done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 34.sp,
            lineHeight = 40.sp,
            color = colorMain
        ),
        h2 = TextStyle( //done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 26.sp,
            color = colorMain
        ),
        h3 = TextStyle( // done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            color = colorMain
        ),
        paragraph = TextStyle( // done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = colorMain
        ),
        buttonText = TextStyle( // done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 26.sp,
            color = colorMain
        ),
        field = TextStyle(
            fontFamily = openSansFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = colorMain
        ),
        fieldLabel = TextStyle( //done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            color = colorMain
        ),
        fieldTextNormal = TextStyle( //done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 26.sp,
            color = colorMain
        ),
        fieldInactive = TextStyle( //done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 26.sp,
            color = colorMain
        ),
        hint = TextStyle( //done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = colorMain
        ),
        left = TextStyle( //done
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            color = colorMain
        ),
        time = TextStyle(
            fontFamily = openSansFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = colorMain
        ),
        small = TextStyle(
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = colorMain
        ),
        bold = TextStyle(
            fontFamily = openSansFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = colorMain
        ),
    )
}