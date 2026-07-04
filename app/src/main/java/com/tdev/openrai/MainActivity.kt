package com.tdev.openrai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tdev.openrai.data.ChatViewModel
import com.tdev.openrai.ui.ApiKeyScreen
import com.tdev.openrai.ui.ChatScreen
import com.tdev.openrai.ui.theme.Bg
import com.tdev.openrai.ui.theme.OpenRouteAiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenRouteAiTheme {
                val vm: ChatViewModel = viewModel()
                val state by vm.state.collectAsState()

                if (state.showApiKeyScreen) {
                    ApiKeyScreen(onSave = vm::saveApiKey)
                } else {
                    ChatScreen(
                        state = state,
                        onSend = vm::sendMessage,
                        onNewChat = vm::newChat,
                        onToggleModelPicker = vm::toggleModelPicker,
                        onSelectModel = vm::selectModel,
                        onChangeApiKey = vm::changeApiKey
                    )
                }
            }
        }
    }
}
