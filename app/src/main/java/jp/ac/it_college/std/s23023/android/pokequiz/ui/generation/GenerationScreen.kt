package jp.ac.it_college.std.s23023.android.pokequiz.ui.generation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.ac.it_college.std.s23023.android.pokequiz.data.entity.GenerationEntity
import jp.ac.it_college.std.s23023.android.pokequiz.mock.GenerationsRepositoryMock
import jp.ac.it_college.std.s23023.android.pokequiz.mock.PokeApiServiceMock
import jp.ac.it_college.std.s23023.android.pokequiz.ui.quiz.QuizViewModel

@Composable
fun GenerationScreen(
    modifier: Modifier = Modifier,
    onGenerationSelected: (Int) -> Unit = {},
    viewModel: GenerationViewModel = hiltViewModel(),
    quizviewModel: QuizViewModel = hiltViewModel()
) {
    val uiState: GenerationUiState by viewModel.uiState.collectAsState()
    val generations: List<GenerationEntity> by uiState.generations.collectAsState(initial = emptyList())

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "世代選択", modifier = Modifier.padding(bottom = 16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else {
            // LazyColumnで各世代ボタンを動的に作成
            LazyColumn {
                items(generations) { generation ->
                    Button(
                        onClick  = {
                            quizviewModel.loadQuizData(generation.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = "${generation.name} (${generation.region})")
                    }
                }

                // 全世代から出題ボタン
                item {
                    Button(
                        onClick = {
                            quizviewModel.loadQuizData(0)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = "全世代から出題")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GenerationScreenPreview() {
    GenerationScreen(
        viewModel = GenerationViewModel(
            repository = GenerationsRepositoryMock,
            service = PokeApiServiceMock
        )
    )
}
