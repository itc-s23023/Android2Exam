package jp.ac.it_college.std.s23023.android.pokequiz.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    tableName = "pokemon_introduced_generation_cross_ref",
    primaryKeys = ["generation_id", "pokemon_id"]
)
data class PokemonIntroducedGenerationCrossRef(
    @ColumnInfo(name = "generation_id") val generationId: Int = 0,
    @ColumnInfo(name = "pokemon_id") val pokemonId: Int = 0,
)

data class GenerationWithPokemon(
    @Embedded val generation: GenerationEntity,
    @Relation(
        parentColumn = "id",  // GenerationEntity の実際のカラム名
        entityColumn = "id",  // PokemonEntity の実際のカラム名
        associateBy = Junction(
            value = PokemonIntroducedGenerationCrossRef::class,
            parentColumn = "generation_id",
            entityColumn = "pokemon_id"
        )
    )
    val pokemon: List<PokemonEntity>
)

