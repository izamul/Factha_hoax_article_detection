package dicoding.android.textclassification.response

import com.google.gson.annotations.SerializedName

data class ResponseArticle(

	@field:SerializedName("data")
	val data: List<ResponseArticleItem>
)

data class ResponseArticleItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("hasil")
	val hasil: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("article")
	val article: String
)

data class ResponseResult(
	@SerializedName("hasil")
	val hasil: Int
) {
	override fun toString(): String {
		return "Result: $hasil"
	}
}