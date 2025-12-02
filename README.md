# 🧠 ZenUp Kotlin Client

Este projeto é um **cliente Kotlin/JVM** que consome a API do ZenUp (backend em FastAPI), incluindo:

- Login  
- Envio de mensagens ao chatbot (Groq LLM)  
- Geração de resumo a partir do histórico de conversa  
- Autenticação via token  
- Integração com Retrofit + OkHttp  
- Logging completo para debug  

A arquitetura está organizada de forma limpa, robusta e escalável.

---

# 📁 Estrutura do Projeto
<img width="430" height="430" alt="image" src="https://github.com/user-attachments/assets/f4ecded7-6656-437e-b874-9f8050272271" />

---

# 🧩 Main.kt  
Arquivo principal do projeto.  
Ele executa o fluxo completo:

1. Login (retorna um token)  
2. Envio de mensagem ao chatbot  
3. Solicitação de resumo  
4. Exibição dos logs completos no console  

Serve como ponto de entrada da aplicação.

---

# 🧩 api/ZenUpApi.kt  
Interface Retrofit que define os endpoints da API:
@POST("api/login")
suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

@POST("api/chat")
suspend fun chat(@Body req: ChatRequest): Response<ChatResponse>

@GET("api/resumo/{id_usuario}")
suspend fun resumo(@Path("id_usuario") id: Long): Response<ResumoResponse>
Ela representa o contrato HTTP do backend.

---

# 🧩 model/  
Contém todas as classes de transporte de dados (DTOs).

### LoginRequest.kt
data class LoginRequest(val chave: String)

### LoginResponse.kt
data class LoginResponse(val token: String)

### ChatRequest.kt
data class ChatRequest(val id: Long, val texto: String)

### ChatResponse.kt
data class ChatResponse(val mensagem: String)

### ResumoResponse.kt
data class ResumoResponse(val resumo: String)

---

# 🧩 network/ — Camada de rede (Retrofit + OkHttp)

## ApiClient.kt  
Singleton responsável por:

- Construir o Retrofit  
- Armazenar o token de autenticação  
- Injetar o OkHttpClient configurado  

object ApiClient {
var authToken: String? = null
val api: ZenUpApi = Retrofit.Builder()
.baseUrl(BASE_URL)
.client(client)
.addConverterFactory(GsonConverterFactory.create())
.build()
.create(ZenUpApi::class.java)
}

---

## AuthInterceptor.kt  
Intercepta TODAS as requisições para:

- Adicionar **Authorization: Bearer token** automaticamente  
- Adicionar header `Accept: application/json`  
- Fazer logging completo:

========== REQUEST DEBUG ==========

URL

METHOD

HEADERS

BODY


E depois:

========== RESPONSE DEBUG ==========

STATUS CODE

HEADERS

BODY

É responsável pela autenticação automática e pela inspeção profunda das requisições.

---

## provideOkHttpClient.kt  
Configura o OkHttpClient com:

- Logging BODY (máximo possível)  
- AuthInterceptor customizado  
- Timeouts (connect / read / write)  

val client = OkHttpClient.Builder()
.addInterceptor(logging)
.addInterceptor(AuthInterceptor(tokenProvider))
.connectTimeout(30, TimeUnit.SECONDS)
.readTimeout(30, TimeUnit.SECONDS)
.writeTimeout(30, TimeUnit.SECONDS)
.build()

---

# 🏗️ Fluxo Completo da Aplicação

1️⃣ O usuário envia sua chave → `/api/login`  
2️⃣ Backend valida e retorna o token  
3️⃣ O token é salvo em `ApiClient.authToken`  
4️⃣ AuthInterceptor injeta o token automaticamente  
5️⃣ O usuário faz requisição ao chatbot → `/api/chat`  
6️⃣ O backend retorna resposta do LLM  
7️⃣ O cliente solicita o resumo → `/api/resumo/{id}`  
8️⃣ Backend consulta Redis, gera resumo e responde  

Tudo isso aparece com logs ricos no console.

---

# 🚀 Como rodar o projeto

### 1. Build
./gradlew build

### 2. Certifique-se que seu backend está rodando:
uvicorn main:app --reload

### 3. Execute o projeto Kotlin:
No IntelliJ → Run Main.kt  
ou
./gradlew run

---

# 🔐 Segurança

A chave de API está atualmente definida diretamente no `Main.kt`.  
Para produção, recomenda-se:

- Variáveis de ambiente  
- Arquivo `.env`  
- System properties  
- Vaults  

(O projeto pode ser atualizado para isso facilmente.)

---

# 🧪 Testes esperados

### Login:
STATUS CODE: 200  
Token recebido: xxxxxxxxx  

### Chat:
STATUS CODE: 200  
mensagem: "texto do chatbot"  

### Resumo:
STATUS CODE: 200  
resumo: "texto resumido"  

---

# 🟩 Tecnologias Utilizadas

| Tecnologia | Descrição |
|-----------|-----------|
| Kotlin | Linguagem principal |
| Coroutines | Execução assíncrona |
| Retrofit | Cliente HTTP |
| OkHttp | Interceptores + transporte |
| Gson | Serialização JSON |
| FastAPI (backend) | API consumida |
| Redis | Histórico da conversa |

---

# 🎯 Conclusão

Este projeto implementa um cliente Kotlin completo, seguro e totalmente debugado, ideal para consumir a API do ZenUp com:

- Código limpo  
- Arquitetura organizada  
- Logging profundo  
- Fluxo de autenticação real  
- DTOs bem definidos  
- Camada de rede sólida com Retrofit + OkHttp  

Pronto para uso, extensão e integração em aplicações reais.
