package tech.framti.domain.action

import kotlinx.coroutines.flow.Flow
import tech.framti.domain.model.AuthState

interface ScratchCardAction {

    val activationState: Flow<AuthState>

    val scratchedCode: Flow<String?>

    suspend fun scratchCard()

    suspend fun activate(code: String)

    suspend fun deactivate()

}