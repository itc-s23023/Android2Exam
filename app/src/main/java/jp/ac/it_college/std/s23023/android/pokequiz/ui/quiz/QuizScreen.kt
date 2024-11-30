package jp.ac.it_college.std.s23023.android.pokequiz.ui.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    toResult: (Int, Int) -> Unit = { _, _ -> }
) {
    // 状態を保持する「第〜問」
    val questionText = remember { mutableStateOf("第1問") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 最上部に「第〜問」
        Text(
            text = questionText.value,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "これだ〜れだ？",
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        // 画像を配置する領域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // 画面の上下割合を調整
            contentAlignment = Alignment.Center
        ) {
            // 画像を入れる
            // Image(
            //     painter = painterResource(id = R.drawable.sample_image), // 適切な画像リソースID
            //     contentDescription = null,
            //     contentScale = ContentScale.Crop,
            //     modifier = Modifier.fillMaxSize()
            // )
        }

        // ボタンを縦に4つ並べる
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // ボタン間の間隔
        ) {
            repeat(4) { index ->
                Button(
                    onClick = {
                        toResult(index + 1, 5) // 仮のデータ
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "ボタン${index + 1}")
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuizScreenPreview() {
    QuizScreen()
}
