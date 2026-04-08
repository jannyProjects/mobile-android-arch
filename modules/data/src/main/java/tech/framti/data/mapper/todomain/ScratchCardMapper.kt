package tech.framti.data.mapper.todomain

import tech.framti.api.model.ScratchCardDto
import tech.framti.domain.model.ScratchCard
import tech.mappie.api.ObjectMappie

object ScratchCardMapper : ObjectMappie<ScratchCardDto, ScratchCard>() {
    override fun map(from: ScratchCardDto) = mapping {
        ScratchCard::android fromProperty ScratchCardDto::android transform {
            it.toInt()
        }
    }
}
