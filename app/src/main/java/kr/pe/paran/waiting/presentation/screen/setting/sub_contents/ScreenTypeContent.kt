package kr.pe.paran.waiting.presentation.screen.setting.sub_contents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.pe.paran.waiting.domain.model.ScreenMode
import kr.pe.paran.waiting.presentation.component.RadioItemButton

@Preview(showBackground = true)
@Composable
fun ScreenTypeContent(
    selectedIndex: ScreenMode = ScreenMode.WAITING,
    onClick: (ScreenMode) -> Unit = {}
) {

    val screenTypes = ScreenMode.values().filter { it != ScreenMode.NONE }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 32.dp)
    ) {
        items(items = screenTypes) { item ->
            RadioItemButton(
                selected = selectedIndex == item,
                title = item.korean,
                onClick = { onClick(item) }
            )
            Divider()
        }
    }
}

