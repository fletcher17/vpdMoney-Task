package com.example.vpdmoneytask.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.vpdmoneytask.MainActivity
import com.example.vpdmoneytask.R
import com.example.vpdmoneytask.databinding.FragmentSingleUserDetailsBinding
import com.example.vpdmoneytask.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_single_user_details.*

class SingleUserDetailsFragment : Fragment(R.layout.fragment_single_user_details) {

    private var _binding: FragmentSingleUserDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: UserViewModel

    private val REQUEST_CODE = 200



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSingleUserDetailsBinding.bind(view)

        viewModel = (activity as MainActivity).userViewModel

        viewModel.singleUserItem.observe(viewLifecycleOwner, Observer {

            binding.nameValueTv.text = it.name
            binding.userNameValueTv.text = it.username
            binding.emailValueTv.text = it.email
            binding.streetValueTv.text = it.address?.street
            binding.suiteValueTv.text = it.address?.suite
            binding.cityValueTv.text = it.address?.city
        })


        binding.avatarImage.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            avatarImage.setImageBitmap(data.extras?.get("data") as Bitmap)
        }
    }
}