package org.example

import kotlinx.coroutines.runBlocking
import org.example.model.ChatRequest
import org.example.model.LoginRequest
import org.example.network.ApiClient

fun main() = runBlocking {

    // 1. Login
    val loginResp = ApiClient.api.login(LoginRequest(chave = "gss_zC9ZzqP4NKPHLZ8FoWayWGdyb3FYJvVByKzP8kcRg0UNp22xPni9apl"))

    if (!loginResp.isSuccessful) {
        println("Login falhou: ${loginResp.code()} ${loginResp.errorBody()?.string()}")
        return@runBlocking
    }

    val token = loginResp.body()?.token ?: run {
        println("Login falhou: token nulo")
        return@runBlocking
    }

    ApiClient.authToken = token
    println("Token recebido: $token")

    // 2. Chat (sem passar Authorization – automaticamente via interceptor)
    val chatResp = ApiClient.api.chat(
        ChatRequest(id = 123, texto = "estou me sentindo muito triste")
    )

    if (chatResp.isSuccessful) {
        println("Resposta do chat: ${chatResp.body()?.mensagem}")
    } else {
        println("Erro chat: ${chatResp.code()} ${chatResp.errorBody()?.string()}")
    }

    // 3. Resumo
    val resumoResp = ApiClient.api.resumo(id = 123)

    if (resumoResp.isSuccessful) {
        println("Resumo: ${resumoResp.body()?.resumo}")
    } else {
        println("Erro resumo: ${resumoResp.code()} ${resumoResp.errorBody()?.string()}")
    }
}
