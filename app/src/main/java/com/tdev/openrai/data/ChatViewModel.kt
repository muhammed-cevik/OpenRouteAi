package com.tdev.openrai.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tdev.openrai.model.ChatMessage
import com.tdev.openrai.model.FREE_MODELS
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val apiKey: String = "",
    val selectedModelId: String = FREE_MODELS[0].id,
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showApiKeyScreen: Boolean = true,
    val showModelPicker: Boolean = false
)

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = AppPrefs(application)
    private val client = OpenRouterClient()

    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state

    init {
        viewModelScope.launch {
            combine(prefs.apiKey, prefs.selectedModel) { key, model ->
                Pair(key, model)
            }.collect { (key, model) ->
                _state.update {
                    it.copy(
                        apiKey = key,
                        selectedModelId = model,
                        showApiKeyScreen = key.isBlank()
                    )
                }
            }
        }
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch {
            prefs.saveApiKey(key)
        }
    }

    fun selectModel(modelId: String) {
        viewModelScope.launch {
            prefs.saveModel(modelId)
            _state.update { it.copy(showModelPicker = false) }
        }
    }

    fun toggleModelPicker() {
        _state.update { it.copy(showModelPicker = !it.showModelPicker) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun newChat() {
        _state.update { it.copy(messages = emptyList(), error = null) }
    }

    fun changeApiKey() {
        _state.update { it.copy(showApiKeyScreen = true) }
    }

    fun sendMessage(text: String) {
        val state = _state.value
        if (text.isBlank() || state.isLoading) return

        val userMsg = ChatMessage(role = "user", content = text)
        val loadingMsg = ChatMessage(role = "assistant", content = "", isLoading = true)
        val newMessages = state.messages + userMsg + loadingMsg

        _state.update { it.copy(messages = newMessages, isLoading = true, error = null) }

        viewModelScope.launch {
            val result = client.sendMessage(
                apiKey = state.apiKey,
                modelId = state.selectedModelId,
                history = newMessages.filter { !it.isLoading }
            )

            result.fold(
                onSuccess = { reply ->
                    _state.update {
                        it.copy(
                            messages = it.messages.dropLast(1) + ChatMessage("assistant", reply),
                            isLoading = false
                        )
                    }
                },
                onFailure = { err ->
                    _state.update {
                        it.copy(
                            messages = it.messages.dropLast(1),
                            isLoading = false,
                            error = err.message ?: "Hata olustu"
                        )
                    }
                }
            )
        }
    }
}
