package io.keyless.scenariodeveloperquickstart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentViewModel : ViewModel() {

    private val apiKey: String = BuildConfig.API_KEY
    private val hosts = listOf(BuildConfig.HOST)

    private val _uiButtonState = MutableStateFlow(EnabledButtonState())
    val uiButtonState: StateFlow<EnabledButtonState> = _uiButtonState.asStateFlow()

    private val _uiIndicatorState = MutableStateFlow(IndicatorState())
    val uiIndicatorState: StateFlow<IndicatorState> = _uiIndicatorState.asStateFlow()

    fun setup() {
        viewModelScope.launch {
            _uiIndicatorState.value = _uiIndicatorState.value.copy(setup = ApiState.Loading)
            keylessWrapper.setup(apiKey = apiKey, hosts = hosts).fold(
                onSuccess = {
                    _uiButtonState.value = _uiButtonState.value.copy(enroll = true)
                    _uiIndicatorState.value = _uiIndicatorState.value.copy(setup = ApiState.Success)
                    checkEnroll()
                },
                onFailure = {
                    Log.e("ContentViewModel", "Keyless setup error.\n ${it.stackTrace}")
                    _uiIndicatorState.value = _uiIndicatorState.value.copy(setup = ApiState.Error)
                }
            )
        }
    }

    private suspend fun checkEnroll() {
        keylessWrapper.isUserAndDeviceActive().fold(
            onSuccess = {
                _uiButtonState.value = _uiButtonState.value.copy(enroll = false, auth = true, deEnroll = true)
                Log.i("ContentViewModel", "Keyless isUserAndDeviceActive success")

            },
            onFailure = {
                _uiButtonState.value = _uiButtonState.value.copy(enroll = true, auth = false, deEnroll = false)
                Log.e("ContentViewModel", "Keyless isUserAndDeviceActive error.\n ${it.stackTrace}")
            })
    }

    fun enroll() {
        viewModelScope.launch {
            _uiIndicatorState.value = _uiIndicatorState.value.copy(enroll = ApiState.Loading)
            keylessWrapper.enroll().fold(
                onSuccess = {
                    checkEnroll()
                    _uiIndicatorState.value = _uiIndicatorState.value.copy(
                        enroll = ApiState.Success,
                        auth = ApiState.Idle,
                        deEnroll = ApiState.Idle
                    )
                    Log.i("ContentViewModel", "Keyless enrollment success")
                },
                onFailure = {
                    _uiIndicatorState.value = _uiIndicatorState.value.copy(
                        enroll = ApiState.Error,
                        auth = ApiState.Idle,
                        deEnroll = ApiState.Idle
                    )
                    Log.e("ContentViewModel", "Keyless enrollment error.\n ${it.stackTrace}")
                }
            )
        }
    }

    fun authenticate() {
        viewModelScope.launch {
            _uiIndicatorState.value = _uiIndicatorState.value.copy(auth = ApiState.Loading)
            keylessWrapper.authenticate().fold(
                onSuccess = {
                    _uiIndicatorState.value = _uiIndicatorState.value.copy(
                        auth = ApiState.Success,
                        enroll = ApiState.Idle,
                        deEnroll = ApiState.Idle
                    )
                    Log.i("ContentViewModel", "Keyless authentication success")
                },
                onFailure = {
                    _uiIndicatorState.value = _uiIndicatorState.value.copy(
                        auth = ApiState.Error,
                        enroll = ApiState.Idle,
                        deEnroll = ApiState.Idle
                    )
                    Log.e("ContentViewModel", "Keyless authentication error.\n ${it.stackTrace}")
                }
            )
        }
    }

    fun deEnroll() {
        viewModelScope.launch {
            _uiIndicatorState.value = _uiIndicatorState.value.copy(deEnroll = ApiState.Loading)
            keylessWrapper.deEnroll().fold(
                onSuccess = {
                    checkEnroll()
                    _uiIndicatorState.value = _uiIndicatorState.value.copy(
                        deEnroll = ApiState.Success,
                        enroll = ApiState.Idle,
                        auth = ApiState.Idle
                    )
                    _uiButtonState.value = _uiButtonState.value.copy(enroll = true, auth = false, deEnroll = false)
                    Log.i("ContentViewModel", "Keyless deEnrollment success")
                },
                onFailure = {
                    _uiIndicatorState.value = _uiIndicatorState.value.copy(
                        deEnroll = ApiState.Error,
                        enroll = ApiState.Idle,
                        auth = ApiState.Idle
                    )
                    Log.e("ContentViewModel", "Keyless deEnrollment failure.\n ${it.stackTrace}")
                }
            )
        }
    }

    fun reset() {
        viewModelScope.launch {
            keylessWrapper.reset().fold(
                onSuccess = {
                    Log.i("ContentViewModel", "Keyless reset success")
                    checkEnroll()
                    _uiIndicatorState.value = IndicatorState(
                        setup = ApiState.Idle,
                        enroll = ApiState.Idle,
                        auth = ApiState.Idle,
                        deEnroll = ApiState.Idle
                    )
                },
                onFailure = {
                    Log.e("ContentViewModel", "Keyless reset failure.\n ${it.stackTrace}")
                }
            )
        }
    }

    companion object {
        val keylessWrapper = KeylessWrapper()
    }
}