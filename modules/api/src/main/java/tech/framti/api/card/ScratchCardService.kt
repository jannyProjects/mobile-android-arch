package tech.framti.api.card

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tech.framti.api.model.ScratchCardDto

interface ScratchCardService {

    @GET("/version")
    suspend fun activate(
        @Query("code") code: String
    ): Response<ScratchCardDto>

}