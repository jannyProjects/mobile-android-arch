package tech.framti.data.api.card

import tech.framti.domain.model.ScratchCard

interface ScratchCardApi {

    suspend fun activate(code: String): ScratchCard
}