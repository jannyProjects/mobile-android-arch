package tech.framti.caml.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.framti.data.action.ScratchCardActionImpl
import tech.framti.domain.action.ScratchCardAction

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardDataActionModule {

    @Binds
    abstract fun bindUserDataAction(action: ScratchCardActionImpl): ScratchCardAction
}
