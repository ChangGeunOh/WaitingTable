package kr.pe.paran.waiting.presentation.screen.setting.sub_contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.R

@Preview(showBackground = true, widthDp = 560, heightDp = 480)
@Composable
fun AppInfoContent(storeVersion: String = "") {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        val context = LocalContext.current
        val packageManager = context.packageManager
        val appVersion = packageManager.getPackageInfo(context.packageName, 0).versionName

        val isExistNewVersion = storeVersion.toString() > appVersion.toString()
        //  "새로운 버전이 나왔습니다." else "최신 버전을 사용 중입니다."

        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.width(180.dp),
            painter = painterResource(id = R.drawable.img_title),
            contentDescription = "Logo Image"
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (storeVersion.isNotEmpty()) {
            Text(
                text = if (isExistNewVersion) "새로운 버전이 나왔습니다." else "최신 버전을 사용 중입니다.",
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (isExistNewVersion) {
            Text(
                text = "신규 버전 $storeVersion",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Text(
            text = "현재 버전 $appVersion",
            color = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "지원환경 Android OS 9.0 (Pie) 이상",
            style = MaterialTheme.typography.body1,
            color = Color.LightGray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

    }
}


