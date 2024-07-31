package ua.czrblz.vrg_soft_test_task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.czrblz.domain.model.RedditChildren
import ua.czrblz.vrg_soft_test_task.databinding.ItemPostBinding
import ua.czrblz.vrg_soft_test_task.listener.OpenPictureListener
import ua.czrblz.vrg_soft_test_task.utils.convertTimestamp
import ua.czrblz.vrg_soft_test_task.utils.isInListOfImageExtensions
import ua.czrblz.vrg_soft_test_task.utils.loadPictureThumbnail

class PostsAdapter(
    context: Context,
    private val openPictureListener: OpenPictureListener
) : PagingDataAdapter<RedditChildren, PostViewHolder>(PostDiffCallback) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            holder.bind(post, openPictureListener)
        }
    }
}

class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: RedditChildren, openPictureListener: OpenPictureListener) {
        post.data.also { redditPost ->
            with(binding) {
                author.text = redditPost.authorName
                timestamp.text = redditPost.createdTimestamp.convertTimestamp()
                commentsCount.text = redditPost.countComments.toString()
                imageView.loadPictureThumbnail(redditPost.thumbnailUrl)
                imageView.setOnClickListener {
                    if (redditPost.thumbnailUrl.isInListOfImageExtensions())
                        openPictureListener.openPicture(redditPost.imageUrl, redditPost.thumbnailUrl)
                }
            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<RedditChildren>() {
    override fun areItemsTheSame(oldItem: RedditChildren, newItem: RedditChildren): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RedditChildren, newItem: RedditChildren): Boolean {
        return oldItem.data.postName == newItem.data.postName
    }
}