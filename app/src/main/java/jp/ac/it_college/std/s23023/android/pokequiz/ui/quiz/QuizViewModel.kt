package jp.ac.it_college.std.s23023.android.pokequiz.ui.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.it_college.std.s23023.android.pokequiz.data.dao.PokemonIntroducedGenerationDao
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val pokemonDao: PokemonIntroducedGenerationDao
) : ViewModel() {

    fun loadQuizData(generation: Int) {
        Log.d("QuizViewModel", "loading generation $generation")
        viewModelScope.launch {
            try {
                // 世代情報とポケモン情報を取得
                pokemonDao.getGenerationWithPokemon(generation).collect { generationWithPokemon ->
                    // 取得したポケモンリストをログに表示
                    generationWithPokemon.pokemon.forEach { pokemon ->
                        Log.d("QuizViewModel", "Pokemon: ${pokemon.name}, ID: ${pokemon.id}")
                    }
                }
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Failed to load data: ${e.message}", e)
            }
        }
    }
}

