package ua.czrblz.vrg_soft_test_task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.czrblz.vrg_soft_test_task.utils.loadPictureThumbnail
import ua.czrblz.domain.model.RedditChildren
import ua.czrblz.vrg_soft_test_task.R
import ua.czrblz.vrg_soft_test_task.listener.OpenPictureListener
import ua.czrblz.vrg_soft_test_task.utils.convertTimestamp
import ua.czrblz.vrg_soft_test_task.utils.listOfImageExtensions

class PostsAdapter(context: Context, private val openPictureListener: OpenPictureListener) : PagingDataAdapter<RedditChildren, PostViewHolder>(PostDiffCallback) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = layoutInflater.inflate(R.layout.item_post, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            holder.bind(post, openPictureListener)
        }
    }
}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val author = itemView.findViewById<TextView>(R.id.author)
    private val timestamp = itemView.findViewById<TextView>(R.id.timestamp)
    private val commentsCount = itemView.findViewById<TextView>(R.id.comments_count)
    private val image = itemView.findViewById<ImageView>(R.id.imageView)

    fun bind(post: RedditChildren, openPictureListener: OpenPictureListener) {
        post.data.also { redditPost ->
            author.text = redditPost.subreddit
            timestamp.text = redditPost.created.convertTimestamp()
            commentsCount.text = redditPost.num_comments.toString()
            image.loadPictureThumbnail(redditPost.thumbnail)
            image.setOnClickListener {
                if (redditPost.thumbnail.split(".").last() in listOfImageExtensions)
                    openPictureListener.openPicture(redditPost.url_overridden_by_dest, redditPost.thumbnail)
            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<RedditChildren>() {
    override fun areItemsTheSame(oldItem: RedditChildren, newItem: RedditChildren): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RedditChildren, newItem: RedditChildren): Boolean {
        return oldItem.data.name == newItem.data.name
    }
}