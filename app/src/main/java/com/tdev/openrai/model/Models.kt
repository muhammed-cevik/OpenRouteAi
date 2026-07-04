package com.tdev.openrai.model

data class ChatMessage(
    val role: String,   // "user" | "assistant"
    val content: String,
    val isLoading: Boolean = false
)

data class AiModel(
    val id: String,
    val displayName: String
)

// OpenRouter ücretsiz modeller
val FREE_MODELS = listOf(
    AiModel("meta-llama/llama-3.3-70b-instruct:free",   "Llama 3.3 70B"),
    AiModel("meta-llama/llama-3.1-8b-instruct:free",    "Llama 3.1 8B"),
    AiModel("google/gemma-3-27b-it:free",               "Gemma 3 27B"),
    AiModel("google/gemma-3-12b-it:free",               "Gemma 3 12B"),
    AiModel("mistralai/mistral-7b-instruct:free",       "Mistral 7B"),
    AiModel("deepseek/deepseek-r1:free",                "DeepSeek R1"),
    AiModel("deepseek/deepseek-chat-v3-0324:free",      "DeepSeek V3"),
    AiModel("qwen/qwen3-8b:free",                       "Qwen3 8B"),
    AiModel("microsoft/phi-4-reasoning:free",           "Phi-4 Reasoning"),
)
