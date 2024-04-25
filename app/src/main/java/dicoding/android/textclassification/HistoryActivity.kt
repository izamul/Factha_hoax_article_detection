package dicoding.android.textclassification

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dicoding.android.textclassification.response.retrofit.ApiConfig
import dicoding.android.textclassification.response.ResponseArticle
import dicoding.android.textclassification.databinding.ActivityHistoryBinding
import dicoding.android.textclassification.response.ResponseArticleItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        fetchData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchData() {
        val client = ApiConfig.getApiService().getArticles()

        client.enqueue(object : Callback<ResponseArticle> {
            override fun onResponse(
                call: Call<ResponseArticle>,
                response: Response<ResponseArticle>
            ) {
                if (response.isSuccessful) {
                    val historyList = response.body()
                    if (historyList != null) {
                        setRecyclerView(historyList.data)
                    }
                } else {
                    // Handle error
                }
            }

            override fun onFailure(
                call: Call<ResponseArticle>,
                t: Throwable
            ) {
                error(t)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRecyclerView(historyList: List<ResponseArticleItem>) {
        historyAdapter = HistoryAdapter(historyList)
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }
        historyAdapter.notifyDataSetChanged()
    }
}
