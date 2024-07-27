package ua.czrblz.vrg_soft_test_task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.czrblz.domain.model.RedditChildren
import ua.czrblz.vrg_soft_test_task.R

class PostsAdapter(context: Context) : PagingDataAdapter<RedditChildren, PostViewHolder>(PostDiffCallback) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = layoutInflater.inflate(R.layout.item_post, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            holder.bind(post)
        }
    }
}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(post: RedditChildren) {
        itemView.findViewById<TextView>(R.id.title_text).text = post.data.author
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<RedditChildren>() {
    override fun areItemsTheSame(oldItem: RedditChildren, newItem: RedditChildren): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RedditChildren, newItem: RedditChildren): Boolean {
        return oldItem.data == newItem.data
    }
}