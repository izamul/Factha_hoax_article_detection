package dicoding.android.textclassification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mediapipe.tasks.components.containers.Classifications
import dicoding.android.textclassification.databinding.ActivityMainBinding
import dicoding.android.textclassification.response.ResponseResult
import java.text.NumberFormat
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dicoding.android.textclassification.response.retrofit.ApiConfig
import java.util.Locale
import java.util.regex.Pattern

@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }

        binding.btnClassify.setOnClickListener {
            val inputText = binding.edInput.text.toString()
            showLoading()
            postText(inputText)
        }

        binding.fabHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun postText(article: String) {
        val sanitizedArticle = sanitizeArticle(article)
        val requestBody = "{\"article\": \"$sanitizedArticle\"}".toRequestBody("application/json".toMediaTypeOrNull())
        val client = ApiConfig.getApiService().checkArticle(requestBody)

        client.enqueue(object : Callback<ResponseResult> {
            override fun onResponse(
                call: Call<ResponseResult>,
                response: Response<ResponseResult>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        hideLoading()
                        processResult(result)
                    }
                } else {
                    onError("Failed to get response")
                }
            }

            override fun onFailure(
                call: Call<ResponseResult>,
                t: Throwable
            ) {
                onError(t.toString())
            }
        })
    }

    private fun sanitizeArticle(article: String): String {
        var sanitizedArticle = article.replace("\"", "\\\"")
        sanitizedArticle = sanitizedArticle.replace("\n", " ")
        sanitizedArticle = wordopt(sanitizedArticle)
        return sanitizedArticle
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnClassify.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnClassify.isEnabled = true
    }

    private fun onError(error: String) {
        hideLoading()
        Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
    }

    private fun processResult(result: ResponseResult) {
        val hasil = result.hasil
        Log.i("lihat hasil", hasil.toString())
        when (hasil) {
            0 -> binding.tvResult.text = "Kemungkinan Fakta"
            1 -> binding.tvResult.text = "Kemungkinan Hoax"
            else -> binding.tvResult.text = "Error"
        }
    }

    private fun wordopt(berita: String): String {
        var text = berita.lowercase(Locale.ROOT)
        text = text.replace("[.*?]".toRegex(), "")
        text = text.replace("\\W", " ")
        text = text.replace("https?://\\S+|www\\.\\S+".toRegex(), "")
        text = text.replace("<.*?>+".toRegex(), "")
        text = text.replace("\n", "")
        text = text.replace("\\w*\\d\\w*", "")
        return text
    }
}
