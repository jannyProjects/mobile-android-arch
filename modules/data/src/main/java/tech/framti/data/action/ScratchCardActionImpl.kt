package tech.framti.data.action

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.framti.data.api.card.ScratchCardApi
import tech.framti.data.datastore.encrypted.EncryptedAppPrefs
import tech.framti.data.datastore.encrypted.serializer.AuthPreferences
import tech.framti.data.holder.ScratchCardHolder
import tech.framti.domain.action.ScratchCardAction
import tech.framti.domain.exception.ActivationException
import tech.framti.domain.model.AuthState
import java.util.UUID
import javax.inject.Inject

class ScratchCardActionImpl @Inject internal constructor(
    private val scratchCardApi: ScratchCardApi,
    private val encryptedAppPrefs: EncryptedAppPrefs,
    private val scratchCardHolder: ScratchCardHolder
) : ScratchCardAction {

    override val activationState: Flow<AuthState> =
        encryptedAppPrefs.authPreferences.map { it.authState }

    override val scratchedCode: Flow<String?> = scratchCardHolder.getNullableItemFlow()

    override suspend fun scratchCard() {
        delay(2_000)
        val uuid = UUID.randomUUID().toString()
        scratchCardHolder.setItem(uuid)
        encryptedAppPrefs.setAuthPreferences(AuthPreferences(AuthState.SCRATCHED))

    }

    override suspend fun activate(code: String) {
        val response = scratchCardApi.activate(code)
        if (response.android > 277028) {
            encryptedAppPrefs.setAuthPreferences(AuthPreferences(AuthState.ACTIVATED))
        } else {
            throw ActivationException()
        }
    }

    override suspend fun deactivate() {
        scratchCardHolder.reset()
        encryptedAppPrefs.setAuthPreferences(AuthPreferences(AuthState.UNSCRATCHED))
    }
}