package kr.pe.paran.waiting.presentation.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.domain.model.MenuItem
import kr.pe.paran.waiting.ui.theme.SubTitleColor
import kr.pe.paran.waiting.ui.theme.TitleColor

@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun SettingList(
    menuList: List<MenuItem> = listOf(
        MenuItem(id = 0, title = "모드 설정", description = "서버 모드"),
        MenuItem(id = 1, title = "화면 설정", description = "대기 접수 화면"),
        MenuItem(id = 2, title = "앱 정보", description = "Version 2.0")
    ),
    indexSelected: Int = 0,
    onClickMenuItem: (MenuItem) -> Unit = {}
) {

    Logcat.i(menuList.toString())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        menuList.forEachIndexed { index, menuItem ->
            SettingMenuItem(
                menuItem = menuItem,
                onClickMenuItem = onClickMenuItem,
                isSelected = index == indexSelected
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingMenuItem(
    menuItem: MenuItem = MenuItem(title = "모드 설정", description = "서버 모드"),
    onClickMenuItem: (MenuItem) -> Unit = {},
    isSelected: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickMenuItem(menuItem) }
            .background(color = if (isSelected) Color(0x0A000000) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = menuItem.title, fontSize = 16.sp, color = TitleColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = menuItem.description, fontSize = 14.sp, color = SubTitleColor)
    }
}