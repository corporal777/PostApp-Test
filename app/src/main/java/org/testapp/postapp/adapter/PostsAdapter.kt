package org.testapp.postapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.testapp.postapp.databinding.ItemLayoutBinding

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    private val postList = ArrayList<Int>()
    private lateinit var clickListener: PostsAdapter.OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(id: Int, list: List<Int>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun addPosts(list: List<Int>, listener: OnItemClickListener) {
        this.clickListener = listener
        postList.clear()
        postList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(postList[position])
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(id: Int) = with(binding) {
            tvPostId.text = id.toString()
            cardItem.setOnClickListener {
                clickListener.onItemClick(id, postList)
            }
        }
    }

}