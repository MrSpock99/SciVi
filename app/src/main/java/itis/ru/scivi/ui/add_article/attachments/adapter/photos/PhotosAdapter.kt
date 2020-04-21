package itis.ru.scivi.ui.add_article.attachments.adapter.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import itis.ru.scivi.R
import itis.ru.scivi.model.PhotoLocal
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotosAdapter(var list: List<PhotoLocal>, private val clickListener: (PhotoLocal) -> Unit) :
    RecyclerView.Adapter<PhotosAdapter.AttachmentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AttachmentHolder(
            inflater.inflate(
                R.layout.item_photo,
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

    override fun onBindViewHolder(holder: AttachmentHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }

    fun submitList(list: List<PhotoLocal>) {
        val duffResult = DiffUtil.calculateDiff(
            PhotoDiffUtilCallback(
                this.list,
                list
            )
        )
        duffResult.dispatchUpdatesTo(this)
        this.list = list
    }

    class AttachmentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PhotoLocal, clickListener: (PhotoLocal) -> Unit) {
            itemView.tv_attachment_name.text = item.name
            if (item.upload) {
                itemView.iv_attachment_photo.setImageResource(R.drawable.ic_file_upload_white)
            } else {
                Glide.with(itemView)
                    .load(item.url)
                    .into(itemView.iv_attachment_photo)
            }
            itemView.setOnClickListener {
                clickListener(item)
            }
        }
    }

    class PhotoDiffUtilCallback(
        private val oldList: List<PhotoLocal>,
        private val newList: List<PhotoLocal>
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
