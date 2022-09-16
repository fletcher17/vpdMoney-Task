package com.example.vpdmoneytask.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vpdmoneytask.MainActivity
import com.example.vpdmoneytask.R
import com.example.vpdmoneytask.adapter.UserAdapter
import com.example.vpdmoneytask.clicklistener.OnItemClicked
import com.example.vpdmoneytask.databinding.FragmentLikesBinding
import com.example.vpdmoneytask.model.UserDetailsResponseItem
import com.example.vpdmoneytask.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_likes.*

class LikesFragment : Fragment(R.layout.fragment_likes), OnItemClicked {

    private var _binding: FragmentLikesBinding? = null
    private val binding get() = _binding

    lateinit var viewModel: UserViewModel
    lateinit var savedUserAdapter: UserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLikesBinding.bind(view)

        viewModel = (activity as MainActivity).userViewModel
        setUpRecyclerView()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user = savedUserAdapter.differ.currentList[position]
                viewModel.deleteUser(user)
                Snackbar.make(view, "Successfully Deleted User", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveUser(user)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedUserDetails)
        }

        viewModel.getSavedUser().observe(viewLifecycleOwner, Observer { usersDetails ->
            savedUserAdapter.differ.submitList(usersDetails)
        })

    }

    private fun setUpRecyclerView() {
        savedUserAdapter = UserAdapter(this)
        binding?.rvSavedUserDetails?.apply {
            adapter = savedUserAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun clickedItem(user: UserDetailsResponseItem) {
        viewModel.sendDetails(user)
        findNavController().navigate(R.id.singleUserDetailsFragment)
    }

}