# 🌤️ ClimaApp

O **ClimaApp** é um aplicativo Android simples e educativo que permite consultar as condições climáticas de qualquer cidade em tempo real. Este projeto foi desenvolvido para demonstrar o consumo de APIs REST, manipulação de JSON e boas práticas de interface com o usuário.

## 🚀 Funcionalidades

- 🔍 **Busca por cidade:** Digite o nome de qualquer cidade para obter os dados.
- 🌡️ **Temperatura em tempo real:** Exibição da temperatura atual convertida para Celsius.
- 💧 **Umidade:** Informação sobre a umidade relativa do ar.
- ☁️ **Descrição do clima:** Descrição textual (ex: "céu limpo", "chuva leve").
- 🌐 **Conexão com API:** Integração direta com o serviço OpenWeatherMap.

## 🛠️ Tecnologias Utilizadas

- **Kotlin:** Linguagem oficial para desenvolvimento Android moderno.
- **OkHttp:** Biblioteca robusta para realizar requisições HTTP de forma eficiente.
- **Gson:** Biblioteca do Google para converter JSON (formato de resposta da API) em objetos Kotlin.
- **Threads/Concurrency:** Uso de threads para garantir que as requisições de rede não travem a interface do usuário (UI).
- **Material Design:** Componentes visuais seguindo as diretrizes modernas do Android.

## 📋 Pré-requisitos e Configuração

Para rodar este projeto ou entender seu funcionamento, você precisará de uma chave de API (API Key) gratuita.

1.  Crie uma conta em [OpenWeatherMap](https://openweathermap.org/).
2.  Gere sua **API Key**.
3.  No arquivo `MainActivity.kt`, localize a variável `apiKey` e substitua pelo seu código:
    ```kotlin
    private val apiKey = "SUA_CHAVE_AQUI"
    ```

## 🧠 Conceitos Aprendidos (Didático)

Este projeto cobre pilares fundamentais do desenvolvimento Android:

1.  **Permissões:** Uso da permissão de `INTERNET` no `AndroidManifest.xml`.
2.  **Ciclo de Vida:** Inicialização de componentes no `onCreate`.
3.  **Network on Main Thread:** Aprendemos que o Android não permite requisições de rede na Thread principal. Por isso, usamos `thread { ... }` para a requisição e `runOnUiThread { ... }` para atualizar a tela.
4.  **Parsing de JSON:** Criação de `data classes` em Kotlin que espelham a estrutura do JSON retornado pela API.

## 📁 Estrutura do Projeto

- `MainActivity.kt`: Lógica principal, captura de cliques e chamadas de rede.
- `WeatherResponse.kt`: Modelos de dados (POJOs) para mapear a resposta da API.
- `activity_main.xml`: Layout da interface definido em XML.

---
Desenvolvido com fins didáticos para estudo de Android Nativo. 🚀
