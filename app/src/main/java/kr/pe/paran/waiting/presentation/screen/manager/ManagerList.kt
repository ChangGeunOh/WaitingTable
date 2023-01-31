package kr.pe.paran.waiting.presentation.screen.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.domain.model.MenuItem
import kr.pe.paran.waiting.ui.theme.TitleColor

@Preview(showBackground = true, widthDp = 200, heightDp = 320)
@Composable
fun ManagerList(
    menuList: List<MenuItem> = listOf(
        MenuItem(
            id = 0,
            title = "대기 호출",
            icon = Icons.Filled.Campaign
        ),
    ),
    indexSelected: Int = 0,
    onClickMenuItem: (MenuItem) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        menuList.forEachIndexed { index, menuItem ->
            ManagerMenuItem(
                menuItem = menuItem,
                onClickMenuItem = onClickMenuItem,
                isSelected = index == indexSelected
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }

}

@Composable
fun ManagerMenuItem(
    menuItem: MenuItem,
    onClickMenuItem: (MenuItem) -> Unit,
    isSelected: Boolean
) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClickMenuItem(menuItem) }
        .background(color = if (isSelected) Color(0x0A000000) else Color.Transparent)
        .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Icon(imageVector = menuItem.icon!!, contentDescription = menuItem.title, tint = TitleColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = menuItem.title, fontSize = 16.sp, color = TitleColor)

    }
}

