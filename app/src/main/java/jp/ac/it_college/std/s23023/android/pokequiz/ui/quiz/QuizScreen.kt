package jp.ac.it_college.std.s23023.android.pokequiz.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    toResult: (Int, Int) -> Unit = { _, _ -> }
) {
    // 問題の数
    val totalQuestions = 5
    // 現在の問題番号を管理する状態
    val currentIndex = remember { mutableStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 最上部に「第〜問」
        Text(
            text = "第${currentIndex.value + 1}問",
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
            // 画像を入れる（動的にする場合はcurrentIndexに応じて変更できるらしい）
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // ボタン間の間隔
        ) {
            repeat(4) { index ->
                Button(
                    onClick = {
                        if (currentIndex.value < totalQuestions - 1) {
                            // 次の問題に移動
                            currentIndex.value += 1
                        } else {
                            // 最終問題が終わったら結果画面に遷移
                            toResult(currentIndex.value + 1, totalQuestions)
                        }
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
