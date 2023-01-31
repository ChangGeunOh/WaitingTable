package kr.pe.paran.waiting.domain.model

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val icon: ImageVector? = null
)
