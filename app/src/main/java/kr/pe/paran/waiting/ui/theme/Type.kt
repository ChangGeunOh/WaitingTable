package kr.pe.paran.waiting.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.R

private val FontFamily.Companion.NotoSansKR: FontFamily
    get() = FontFamily(
        Font(R.font.noto_sans_kr_black, weight = FontWeight.Black),
        Font(R.font.noto_sans_kr_regular, weight = FontWeight.Normal),
        Font(R.font.noto_sans_kr_bold, weight = FontWeight.Bold),
        Font(R.font.noto_sans_kr_light, weight = FontWeight.Light),
        Font(R.font.noto_sans_kr_medium, weight = FontWeight.Medium),
        Font(R.font.noto_sans_kr_thin, weight = FontWeight.Thin)
    )

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.NotoSansKR,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.No rmal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)