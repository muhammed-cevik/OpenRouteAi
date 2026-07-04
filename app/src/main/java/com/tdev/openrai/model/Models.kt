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

// Temmuz 2026 dogrulanmis ucretsiz OpenRouter modelleri
val FREE_MODELS = listOf(
    AiModel("openrouter/free",                                    "Otomatik (En iyi ucretsiz)"),
    AiModel("meta-llama/llama-3.3-70b-instruct:free",             "Llama 3.3 70B"),
    AiModel("openai/gpt-oss-120b:free",                           "GPT OSS 120B"),
    AiModel("openai/gpt-oss-20b:free",                            "GPT OSS 20B"),
    AiModel("nvidia/nemotron-3-ultra-550b-a55b:free",             "Nemotron Ultra 550B"),
    AiModel("nvidia/nemotron-3-super-120b-a12b:free",             "Nemotron Super 120B"),
    AiModel("nvidia/nemotron-3-nano-30b-a3b:free",                "Nemotron Nano 30B"),
    AiModel("qwen/qwen3-coder:free",                              "Qwen3 Coder"),
    AiModel("google/gemma-4-27b-it:free",                         "Gemma 4 27B"),
    AiModel("nousresearch/hermes-3-llama-3.1-405b:free",          "Hermes 3 405B"),
    AiModel("meta-llama/llama-3.2-3b-instruct:free",              "Llama 3.2 3B (Hizli)"),
    AiModel("liquid/lfm-2.5-1.2b-instruct:free",                  "Liquid LFM 1.2B"),
)
