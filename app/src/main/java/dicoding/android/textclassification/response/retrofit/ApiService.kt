package dicoding.android.textclassification.response.retrofit

import dicoding.android.textclassification.response.ResponseArticle
import dicoding.android.textclassification.response.ResponseResult
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("data")
    fun getArticles(): Call<ResponseArticle>

    @Headers("Content-Type: application/json")
    @POST("predict")
    fun checkArticle(
        @Body article: RequestBody
    ): Call<ResponseResult>
}