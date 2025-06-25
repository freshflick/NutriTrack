package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip


sealed interface UiState {
    /**
     * Empty state when the screen is first shown
     */
    object Initial : UiState
    /**
     * Still loading
     */
    object Loading : UiState

    /**
     * Text has been generated
     */
    data class Success(val outputText: String) : UiState

    /**
     * There was an error generating text
     */
    data class Error(val errorMessage: String) : UiState
}