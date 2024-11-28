package jp.ac.it_college.std.s23023.android.pokequiz.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,       // ポケモンのID
    val name: String   // ポケモンの名前
)
