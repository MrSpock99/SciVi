package itis.ru.scivi.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import itis.ru.scivi.R
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.Attachment
import kotlinx.android.synthetic.main.item_article.view.*

class SearchArticlesAdapter(
    var list: List<ArticleLocal>,
    private val clickListener: (Attachment) -> Unit
) :
    RecyclerView.Adapter<SearchArticlesAdapter.ArticleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticleHolder(
            inflater.inflate(
                R.layout.item_article,
                parent,
                false
            )
        )

    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_photo
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.bind(list[position])
    }

    fun submitList(list: List<ArticleLocal>) {
        val duffResult = DiffUtil.calculateDiff(ArticleDiffUtilCallback(this.list, list))
        duffResult.dispatchUpdatesTo(this)
        this.list = list
    }

    class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ArticleLocal) {
            itemView.tv_article_name.text = item.name
        }
    }

    class ArticleDiffUtilCallback(
        private val oldList: List<ArticleLocal>,
        private val newList: List<ArticleLocal>
    ) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}
