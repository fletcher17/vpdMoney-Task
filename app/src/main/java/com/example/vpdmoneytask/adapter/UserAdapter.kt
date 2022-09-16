package com.example.vpdmoneytask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vpdmoneytask.clicklistener.OnItemClicked
import com.example.vpdmoneytask.databinding.UsersLayoutBinding
import com.example.vpdmoneytask.model.UserDetailsResponseItem

class UserAdapter(val listener: OnItemClicked): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UsersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userDetails = differ.currentList[position]

        holder.bind(userDetails, listener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class UserViewHolder(private val binding:  UsersLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(users: UserDetailsResponseItem, listener: OnItemClicked) {
            binding.nameValueTv.text = users.name
            binding.userNameValueTv.text = users.username
            binding.emailValueTv.text = users.email

            binding.container.setOnClickListener {
                listener.clickedItem(users)
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<UserDetailsResponseItem>() {
        override fun areItemsTheSame(
            oldItem: UserDetailsResponseItem,
            newItem: UserDetailsResponseItem
        ): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(
            oldItem: UserDetailsResponseItem,
            newItem: UserDetailsResponseItem
        ): Boolean {
            return  oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
}