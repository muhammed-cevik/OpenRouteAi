package com.tdev.openrai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tdev.openrai.data.ChatUiState
import com.tdev.openrai.model.AiModel
import com.tdev.openrai.model.ChatMessage
import com.tdev.openrai.model.FREE_MODELS
import com.tdev.openrai.ui.theme.*

@Composable
fun ChatScreen(
    state: ChatUiState,
    onSend: (String) -> Unit,
    onNewChat: () -> Unit,
    onToggleModelPicker: () -> Unit,
    onSelectModel: (String) -> Unit,
    onChangeApiKey: () -> Unit
) {
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val currentModel = FREE_MODELS.find { it.id == state.selectedModelId }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Yeni sohbet",
                color = TextDim,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onNewChat() }
            )
            Text(
                text = currentModel?.displayName ?: "Model",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onToggleModelPicker() }
            )
            Text(
                text = "API",
                color = TextDim,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onChangeApiKey() }
            )
        }

        HorizontalDivider(color = Divider, thickness = 0.5.dp)

        // Model picker dropdown
        if (state.showModelPicker) {
            ModelPicker(
                models = FREE_MODELS,
                selectedId = state.selectedModelId,
                onSelect = onSelectModel
            )
            HorizontalDivider(color = Divider, thickness = 0.5.dp)
        }

        // Mesajlar
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            if (state.messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ne sormak istiyorsun?",
                            color = TextDim,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            items(state.messages) { msg ->
                MessageBubble(msg)
            }
            if (state.error != null) {
                item {
                    Text(
                        text = state.error,
                        color = Color(0xFFFF6B6B),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = Divider, thickness = 0.5.dp)

        // Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Mesaj...", color = TextDim, fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                maxLines = 5,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (input.isNotBlank() && !state.isLoading) {
                        onSend(input)
                        input = ""
                    }
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Divider,
                    unfocusedBorderColor = Divider,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = TextPrimary,
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!state.isLoading && input.isNotBlank()) Color.White else Surface)
                    .clickable(enabled = !state.isLoading && input.isNotBlank()) {
                        onSend(input)
                        input = ""
                    },
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = TextDim,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Gonder",
                        tint = if (input.isNotBlank()) Color.Black else TextDim,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(msg: ChatMessage) {
    val isUser = msg.role == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp, topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(if (isUser) UserBubble else AiBubble)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            if (msg.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = TextDim,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = msg.content,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun ModelPicker(
    models: List<AiModel>,
    selectedId: String,
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(vertical = 8.dp)
    ) {
        models.forEach { model ->
            val selected = model.id == selectedId
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(model.id) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = model.displayName,
                    color = if (selected) TextPrimary else TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                )
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                    )
                }
            }
        }
    }
}
