package dicoding.android.textclassification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dicoding.android.textclassification.response.ResponseArticleItem

class HistoryAdapter(private val historyList: List<ResponseArticleItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvArticle: TextView = itemView.findViewById(R.id.tvArticle)
        val tvStatusArticle: TextView = itemView.findViewById(R.id.tvStatusArticle)
        val tvResult: TextView = itemView.findViewById(R.id.tvResult)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_card, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]

        holder.tvArticle.text = currentItem.article
        holder.tvDate.text = currentItem.date
        holder.tvStatusArticle.text = "Status: "
        holder.tvResult.text = if (currentItem.hasil == 1) "Kemungkinan Hoax" else "Kemungkinan Fakta"
    }

    override fun getItemCount() = historyList.size
}
