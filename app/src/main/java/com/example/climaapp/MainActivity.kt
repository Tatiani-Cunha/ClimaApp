package com.example.climaapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var edtCidade: EditText
    private lateinit var btnBuscar: Button
    private lateinit var txtResultado: TextView

    private val client = OkHttpClient()

    // Troque pela sua chave da OpenWeatherMap
    private val apiKey = "SUA_CHAVE_AQUI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtCidade = findViewById(R.id.edtCidade)
        btnBuscar = findViewById(R.id.btnBuscar)
        txtResultado = findViewById(R.id.txtResultado)
        edtCidade.hint = "Ex.: São Paulo, SP ou São Paulo, BR"

        btnBuscar.setOnClickListener {
            val cidade = edtCidade.text.toString().trim()

            if (cidade.isEmpty()) {
                Toast.makeText(this, "Digite o nome da cidade", Toast.LENGTH_SHORT).show()
            } else {
                buscarClima(cidade)
            }
        }
    }

    private fun buscarClima(cidade: String) {
        txtResultado.text = "Carregando..."

        val entradaNormalizada = cidade.trim()
        val entradaParaApi = entradaNormalizada
            .replace("-", " ")
            .replace(Regex("\\s+"), " ")

        thread {
            val localCorreto = resolverLocalizacao(entradaParaApi)

            if (localCorreto.isNotBlank()) {
                runOnUiThread {
                    txtResultado.text = "Local encontrado: $localCorreto\nCarregando o clima..."
                }
            } else {
                runOnUiThread {
                    txtResultado.text = "Carregando..."
                }
            }

            val localParaConsulta = if (localCorreto.isNotBlank()) {
                localCorreto
            } else {
                entradaParaApi
            }

            val cidadeCodificada = URLEncoder.encode(localParaConsulta, StandardCharsets.UTF_8.toString())
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$cidadeCodificada&appid=$apiKey&lang=pt_br&units=metric"

            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                val body = response.body?.string()

                if (response.isSuccessful && body != null) {
                    val gson = Gson()
                    val weatherResponse = gson.fromJson(body, WeatherResponse::class.java)

                    val tempCelsius = weatherResponse.main.temp
                    val umidade = weatherResponse.main.humidity
                    val descricao = weatherResponse.weather.firstOrNull()?.description ?: "Sem descrição"
                    val cidadeResposta = weatherResponse.name
                    val pais = weatherResponse.sys.country
                    val localFormatado = when {
                        cidadeResposta.isNotBlank() && pais.isNotBlank() -> "$cidadeResposta - $pais"
                        cidadeResposta.isNotBlank() -> cidadeResposta
                        else -> "Local não identificado"
                    }

                    runOnUiThread {
                        txtResultado.text = """
                            Local: $localFormatado
                            Temperatura: %.1f °C
                            Umidade: $umidade%%
                            Clima: $descricao
                        """.trimIndent().format(tempCelsius)
                    }
                } else {
                    val mensagemErro = when (response.code) {
                        404 -> "Cidade não encontrada. Tente outra cidade ou país."
                        401 -> "Chave da API inválida."
                        else -> "Não foi possível obter os dados."
                    }

                    runOnUiThread {
                        txtResultado.text = mensagemErro
                    }
                }
            } catch (e: IOException) {
                runOnUiThread {
                    txtResultado.text = "Erro de conexão. Verifique a internet."
                }
            } catch (e: Exception) {
                runOnUiThread {
                    txtResultado.text = "Erro ao processar os dados."
                }
            }
        }
    }

    private fun resolverLocalizacao(entrada: String): String {
        val consulta = entrada.trim()
        if (consulta.isBlank()) return ""

        val cidadeCodificada = URLEncoder.encode(consulta, StandardCharsets.UTF_8.toString())
        val url = "https://api.openweathermap.org/geo/1.0/direct?q=$cidadeCodificada&limit=1&appid=$apiKey"
        val request = Request.Builder().url(url).build()

        return try {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return ""

            if (!response.isSuccessful) return ""

            val gson = Gson()
            val listType = object : TypeToken<List<GeoLocationResponse>>() {}.type
            val locais: List<GeoLocationResponse> = gson.fromJson(body, listType)

            val melhorLocal = locais.firstOrNull() ?: return ""
            val estado = melhorLocal.state

            if (estado.isNullOrBlank()) {
                melhorLocal.name
            } else {
                "${melhorLocal.name}, ${estado}, ${melhorLocal.country}"
            }
        } catch (_: Exception) {
            ""
        }
    }

}
