package jp.ac.it_college.std.s23023.android.pokequiz.data.repository.impl

import jp.ac.it_college.std.s23023.android.pokequiz.data.dao.PokemonIntroducedGenerationDao
import jp.ac.it_college.std.s23023.android.pokequiz.data.entity.GenerationWithPokemon
import jp.ac.it_college.std.s23023.android.pokequiz.data.entity.PokemonIntroducedGenerationCrossRef
import jp.ac.it_college.std.s23023.android.pokequiz.data.repository.PokemonIntroducedGenerationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonIntroducedGenerationRepositoryImpl @Inject constructor(
    private val dao: PokemonIntroducedGenerationDao
) : PokemonIntroducedGenerationRepository {
    override fun getGenerationWithPokemon(id: Int): Flow<GenerationWithPokemon> =
        dao.getGenerationWithPokemon(id)

    override suspend fun getEntryCount(id: Int): Int = dao.getEntryCount(id)

    override suspend fun upsertEntry(generationId: Int, pokemonId: Int) =
        dao.upsert(PokemonIntroducedGenerationCrossRef(generationId, pokemonId))

    override suspend fun getMissingGenerationIds(maxGenerationId: Int): List<Int> {
        // Dao からすべての存在する generation_id を取得
        val existingIds = dao.getAllExistingGenerationIds()

        // 1 から maxGenerationId までのリストを生成し、存在しない ID を計算
        return (1..maxGenerationId).toList().minus(existingIds)
    }
}