package jp.ac.it_college.std.s23023.android.pokequiz.ui.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.it_college.std.s23023.android.pokequiz.R

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    score: Int,
    onGenerationSelected: () -> Unit = {},
    onGenerationClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // スコア表示
        Text(
            text = stringResource(id = R.string.result_score_message, score),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // ボタン: もう一度遊ぶ
        Button(
            onClick = onGenerationSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.play_again_button))
        }

        // ボタン: 世代選択
        Button(
            onClick = onGenerationClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.select_generation_button))
        }
    }
}


@Preview
@Composable
private fun ResultScreenPreview() {
    ResultScreen(score = 8)
}