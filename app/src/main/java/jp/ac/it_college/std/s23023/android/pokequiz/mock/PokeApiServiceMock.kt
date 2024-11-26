package jp.ac.it_college.std.s23023.android.pokequiz.mock

import jp.ac.it_college.std.s23023.android.pokequiz.network.PokeApiService
import jp.ac.it_college.std.s23023.android.pokequiz.network.model.Generation
import jp.ac.it_college.std.s23023.android.pokequiz.network.model.Named
import jp.ac.it_college.std.s23023.android.pokequiz.network.model.NamedApiResource
import jp.ac.it_college.std.s23023.android.pokequiz.network.model.Region

object PokeApiServiceMock : PokeApiService {
    override suspend fun getGenerations(): Named = Named()

    override suspend fun getGenerationById(id: Int): Generation = Generation(
        id = 1,
        name = "ダミー",
        mainRegion = NamedApiResource(name = "ダミー地方", url = "http://example.com/"),
        names = emptyList(),
        pokemonSpecies = emptyList()
    )

    override suspend fun getGenerationByName(name: String): Generation = getGenerationById(1)

    override suspend fun getRegionByName(name: String): Region = Region(id = 1, names = emptyList())
}