package com.example.vpdmoneytask.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vpdmoneytask.MainActivity
import com.example.vpdmoneytask.R
import com.example.vpdmoneytask.adapter.UserAdapter
import com.example.vpdmoneytask.clicklistener.OnItemClicked
import com.example.vpdmoneytask.databinding.FragmentUsersDetailBinding
import com.example.vpdmoneytask.db.UserDatabase
import com.example.vpdmoneytask.model.Address
import com.example.vpdmoneytask.model.Company
import com.example.vpdmoneytask.model.UserDetailsResponseItem
import com.example.vpdmoneytask.repository.UserRepository
import com.example.vpdmoneytask.util.Resource
import com.example.vpdmoneytask.viewModel.UserViewModel
import com.example.vpdmoneytask.viewModel.UserViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_users_detail.*

class UsersDetailFragment : Fragment(R.layout.fragment_users_detail), OnItemClicked{

    private var _binding: FragmentUsersDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: UserViewModel

    lateinit var userAdapter: UserAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUsersDetailBinding.bind(view)

        viewModel = (activity as MainActivity).userViewModel
        setUpRecyclerView()

        binding.fabMainActivity.setOnClickListener {
            PostUser()
        }

        viewModel.userDetails.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userResponse ->
                        userAdapter.differ.submitList(userResponse)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Snackbar.make(binding.root, "An Error Occurred: $message", Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun setUpRecyclerView() {
        userAdapter = UserAdapter(this)
        binding.rvUserDetails.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun clickedItem(user: UserDetailsResponseItem) {

        viewModel.sendDetails(user)
        findNavController().navigate(R.id.action_usersDetailFragment_to_singleUserDetailsFragment)
    }

    private fun PostUser() {
        val dialog = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.users_posts, null)
        dialog.setView(view)
        val userDetailDialog = dialog.create()
        userDetailDialog.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        userDetailDialog.show()

        val name: EditText = view.findViewById(R.id.dialognameTextView)
        val email: EditText = view.findViewById(R.id.dialogemailTv)
        val userName: EditText = view.findViewById(R.id.dialogusernameTv)
        val street: EditText = view.findViewById(R.id.dialogaddressTv)
        val add: TextView = view.findViewById(R.id.add_post_textView)

        val userAddress = Address(street.text.toString(), street.text.toString(), street.text.toString(), street.text.toString())
        val company = Company("fintech", "Vpd", "VpdMoney")


        add.setOnClickListener {
            if (name.text.toString().isEmpty()) {
                Snackbar.make(view, "Enter your name", Snackbar.LENGTH_LONG).setAnchorView(binding.fabMainActivity).show()
                return@setOnClickListener
            }
            if (email.text.toString().isEmpty()) {
                Snackbar.make(view, "Enter your email", Snackbar.LENGTH_LONG).setAnchorView(binding.fabMainActivity).show()
                return@setOnClickListener
            }
            if (userName.text.toString().isEmpty()) {
                Snackbar.make(view, "Enter your username", Snackbar.LENGTH_LONG).setAnchorView(binding.fabMainActivity).show()
                return@setOnClickListener
            }
            val usersDetail = UserDetailsResponseItem(userAddress, company, email.text.toString(), 0, name.text.toString(), null, userName.text.toString(), street.text.toString())

            //    commentDataManager.saveComments(commentsTyped)
            viewModel.saveUser(usersDetail)
            Log.d("saved", "$usersDetail")
            Snackbar.make(view, "User Detail Saved", Snackbar.LENGTH_LONG).setAnchorView(requireView().rootView.bottom_nav_view).show()

            userDetailDialog.dismiss()
        }
    }

}