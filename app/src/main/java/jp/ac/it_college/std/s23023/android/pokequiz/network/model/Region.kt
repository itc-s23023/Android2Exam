package jp.ac.it_college.std.s23023.android.pokequiz.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Region(
    val id: Int,
    val names: List<Name>
)
