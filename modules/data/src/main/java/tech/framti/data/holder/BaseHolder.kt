package tech.framti.data.holder

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transform

abstract class BaseHolder<T>(val defaultValue: T? = null) {

    private val stateFlow = MutableStateFlow(defaultValue)

    open suspend fun setItem(item: T) {
        stateFlow.value = item
    }

    fun getItemFlow() = stateFlow.transform<T?, T> { value ->
        if (value != null) {
            emit(value)
        }
    }

    fun getNullableItemFlow(): Flow<T?> = stateFlow

    @Suppress("UNCHECKED_CAST")
    fun getItem() = stateFlow.value as T

    fun getItemOrNull() = stateFlow.value as? T

    fun reset() {
        stateFlow.value = defaultValue
    }
}
