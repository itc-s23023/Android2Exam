package jp.ac.it_college.std.s23023.android.pokequiz.ui.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.it_college.std.s23023.android.pokequiz.data.repository.PokemonIntroducedGenerationRepository
import jp.ac.it_college.std.s23023.android.pokequiz.data.dao.PokemonIntroducedGenerationDao
import jp.ac.it_college.std.s23023.android.pokequiz.data.entity.GenerationEntity
import jp.ac.it_college.std.s23023.android.pokequiz.data.entity.GenerationWithPokemon
import jp.ac.it_college.std.s23023.android.pokequiz.data.repository.GenerationsRepository
import jp.ac.it_college.std.s23023.android.pokequiz.network.PokeApiService
import jp.ac.it_college.std.s23023.android.pokequiz.network.model.Name
import jp.ac.it_college.std.s23023.android.pokequiz.network.model.isJa
import jp.ac.it_college.std.s23023.android.pokequiz.network.model.isJaHrkt
import jp.ac.it_college.std.s23023.android.pokequiz.ui.generation.GenerationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class QuizUiState(
    val isLoading: Boolean = true,
    val generations: Flow<List<GenerationEntity>> = emptyFlow(),
    val pokeNames: Flow<GenerationWithPokemon> = emptyFlow()
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val generationId: Int,
    private val repository: PokemonIntroducedGenerationRepository,
    private val genrepository: GenerationsRepository,
    private val service: PokeApiService
): ViewModel() {
    fun loadQuizData(generation: Int) {
        Log.d("QuizViewModel", "loading $generation")
    }

    private val _uiState = MutableStateFlow(
        QuizUiState(
            generations = genrepository.getAllGenerationsStream()
        )
    )
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            if(repository.getEntryCount(generationId) == 0) {
                retrieveAndCacheGenerations()
            }
            updateLoadingState(false)
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    private suspend fun retrieveAndCacheGenerations() {
        Log.d("QuizViewModel", "Fetching generations from API...")

        service.getGenerations().results.forEach { result ->
            Log.d("QuizViewModel", "Fetching details for generation: ${result.name}")

            // 世代情報の詳細を取得
            val gen = service.getGenerationByName(result.name)

            // 各世代に属するポケモンのIDをDBに登録
            gen.pokemonSpecies.forEach { species ->
                Log.d("QuizViewModel", "Fetching details for pokemon: ${species.name}")

                // ポケモンの名前からIDを抽出
                val pokemon = service.getPokemonByName(species.name)

                // Generation ID と Pokemon ID を紐付けてデータベースに保存
                repository.upsertEntry(
                    generationId = gen.id,
                    pokemonId = pokemon.id
                )
                Log.d("QuizViewModel", "Pokemon ${pokemon.name} (ID: ${pokemon.id}) added to generation ${gen.id}")
            }

            Log.d("QuizViewModel", "Completed processing generation: ${gen.name}")
        }

        Log.d("QuizViewModel", "All generations fetched and cached.")
    }

}
