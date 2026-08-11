package com.alan.queensland.core.ui.base.model

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data object Error : UiState<Nothing>
    data class Data<T>(val value: T) : UiState<T>
}
