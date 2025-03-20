package io.keyless.scenariodeveloperquickstart

sealed interface ApiState {
    data object Idle : ApiState
    data object Loading : ApiState
    data object Success : ApiState
    data object Error : ApiState
}

data class IndicatorState(
    val setup: ApiState = ApiState.Idle,
    val enroll: ApiState = ApiState.Idle,
    val auth: ApiState = ApiState.Idle,
    val deEnroll: ApiState = ApiState.Idle,
)

data class EnabledButtonState(
    val setup: Boolean = true,
    val enroll: Boolean = false,
    val auth: Boolean = false,
    val deEnroll: Boolean = false
)
