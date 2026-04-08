package tech.framti.data.api.card

import tech.framti.api.card.ScratchCardService
import tech.framti.data.api.BaseApi
import tech.framti.data.mapper.todomain.ScratchCardMapper
import tech.framti.domain.exception.ApiException
import javax.inject.Inject

class ScratchCardApiImpl
@Inject internal constructor(
    private val scratchCardService: ScratchCardService
) : ScratchCardApi, BaseApi() {

    override suspend fun activate(code: String) = handleApi {
        scratchCardService.activate(code)
    }?.let {
        ScratchCardMapper.map(it)
    } ?: throw ApiException(errorMessage = "ScratchCard API call failed!")
}