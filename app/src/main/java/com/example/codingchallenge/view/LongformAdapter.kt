package com.example.codingchallenge.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallenge.databinding.SingleItemBinding

class LongformAdapter(
    initialList: List<String> = emptyList()
) : ListAdapter<String, LongformAdapter.ViewHolder>(callback) {
    var list: List<String>
        get() = currentList
        set(value) {
            submitList(value)
        }

    init {
        list = initialList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(val binding: SingleItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.item = item
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}
