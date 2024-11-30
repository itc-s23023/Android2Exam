package jp.ac.it_college.std.s23023.android.pokequiz.ui.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.it_college.std.s23023.android.pokequiz.data.repository.PokemonIntroducedGenerationRepository
import jp.ac.it_college.std.s23023.android.pokequiz.data.entity.GenerationEntity
import jp.ac.it_college.std.s23023.android.pokequiz.data.entity.GenerationWithPokemon
import jp.ac.it_college.std.s23023.android.pokequiz.data.repository.GenerationsRepository
import jp.ac.it_college.std.s23023.android.pokequiz.network.PokeApiService
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException



data class QuizUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null, // エラーのメッセージを保持
    val generations: Flow<List<GenerationEntity>> = emptyFlow(),
    val pokeNames: Flow<GenerationWithPokemon> = emptyFlow()
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val generationId: Int,
    private val repository: PokemonIntroducedGenerationRepository,
    private val genrepository: GenerationsRepository,
    private val service: PokeApiService
) : ViewModel() {
    private var generation: Int = generationId

    fun loadQuizData(newGeneration: Int) {
        generation = newGeneration
        Log.d("QuizViewModel", "Loading data for generation: $generation")

        viewModelScope.launch {
            try {
                val entryCount = repository.getEntryCount(generation)
                Log.d("QuizViewModel", "Entry count for generation $generation: $entryCount")

                if (entryCount == 0) {
                    retrieveAndCacheGenerations(generation)
                }
                updateLoadingState(false)
            } catch (e: Exception) {
                handleError("Failed to load quiz data for generation $generation: ${e.message}")
            }
        }
    }

    private val _uiState = MutableStateFlow(
        QuizUiState(
            generations = genrepository.getAllGenerationsStream()
        )
    )
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val missingGenerationIds = repository.getMissingGenerationIds(maxGenerationId = 9)
                val entryCount = repository.getEntryCount(generation)
                Log.d("MissingGenerationIds", "Missing IDs: $missingGenerationIds")
                Log.d("QuizViewModel", "Entry count for initial generation $generation: $entryCount")

                if (entryCount == 0) {
                    retrieveAndCacheGenerations(generation)
                }
                updateLoadingState(false)
            } catch (e: Exception) {
                handleError("Failed to initialize quiz data: ${e.message}")
            }
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading, errorMessage = null)
        }
    }

    private fun handleError(message: String) {
        Log.e("QuizViewModel", message)
        _uiState.update {
            it.copy(isLoading = false, errorMessage = message)
        }
    }

    private suspend fun retrieveAndCacheGenerations(generation: Int) {
        try {
            if (generation == 0) {
                Log.d("QuizViewModel", "Fetching missing generations from API...")
                // 存在しない世代のIDを取得
                val missingGenerationIds = repository.getMissingGenerationIds(maxGenerationId = 9)

                for (genId in missingGenerationIds) {
                    Log.d("QuizViewModel", "Fetching details for generation ID: $genId")
                    try {
                        val gen = service.getGenerationById(genId)

                        gen.pokemonSpecies.forEach { species ->
                            Log.d("QuizViewModel", "Fetching details for pokemon: ${species.name}")
                            try {
                                val pokemon = service.getPokemonByName(species.name)
                                repository.upsertEntry(
                                    generationId = genId,
                                    pokemonId = pokemon.id
                                )
                                Log.d("QuizViewModel", "Pokemon ${pokemon.name} (ID: ${pokemon.id}) added to generation $genId")
                            } catch (e: Exception) {
                                if (e is HttpException && e.code() == 404) {
                                    Log.w("QuizViewModel", "Pokemon ${species.name} not found, skipping.")
                                } else {
                                    throw e // 他のエラーは再スロー
                                }
                            }
                        }
                    } catch (e: Exception) {
                        if (e is HttpException && e.code() == 404) {
                            Log.w("QuizViewModel", "Generation ID $genId not found, skipping.")
                        } else {
                            throw e // 他のエラーは再スロー
                        }
                    }
                }
                Log.d("QuizViewModel", "Missing generations fetched and cached.")
            } else {
                Log.d("QuizViewModel", "Fetching generation $generation from API...")
                val gen = service.getGenerationById(generation)

                gen.pokemonSpecies.forEach { species ->
                    Log.d("QuizViewModel", "Fetching details for pokemon: ${species.name}")
                    try {
                        val pokemon = service.getPokemonByName(species.name)
                        repository.upsertEntry(
                            generationId = generation,
                            pokemonId = pokemon.id
                        )
                        Log.d("QuizViewModel", "Pokemon ${pokemon.name} (ID: ${pokemon.id}) added to generation $generation")
                    } catch (e: Exception) {
                        if (e is HttpException && e.code() == 404) {
                            Log.w("QuizViewModel", "Pokemon ${species.name} not found, skipping.")
                        } else {
                            throw e // 他のエラーは再スロー
                        }
                    }
                }
                Log.d("QuizViewModel", "Completed processing generation: $generation")
            }
        } catch (e: Exception) {
            handleError("Failed to fetch or cache generation data: ${e.message}")
        }
    }


}