package com.tdev.openrai.data

import com.tdev.openrai.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class OpenRouterClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun sendMessage(
        apiKey: String,
        modelId: String,
        history: List<ChatMessage>
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val messages = JSONArray()
            history.filter { !it.isLoading }.forEach { msg ->
                val obj = JSONObject()
                obj.put("role", msg.role)
                obj.put("content", msg.content)
                messages.put(obj)
            }

            val body = JSONObject()
            body.put("model", modelId)
            body.put("messages", messages)

            val request = Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .addHeader("HTTP-Referer", "https://github.com/muhammed-cevik/OpenRouteAi")
                .post(body.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                val errMsg = try {
                    JSONObject(responseBody).optJSONObject("error")?.optString("message") ?: "HTTP ${response.code}"
                } catch (e: Exception) { "HTTP ${response.code}" }
                return@withContext Result.failure(Exception(errMsg))
            }

            val json = JSONObject(responseBody)
            val content = json
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")

            Result.success(content.trim())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
