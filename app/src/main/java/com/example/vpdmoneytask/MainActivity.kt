package com.example.vpdmoneytask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vpdmoneytask.databinding.ActivityMainBinding
import com.example.vpdmoneytask.db.UserDatabase
import com.example.vpdmoneytask.repository.UserRepository
import com.example.vpdmoneytask.viewModel.UserViewModel
import com.example.vpdmoneytask.viewModel.UserViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController : NavController

    private lateinit var listener: NavController.OnDestinationChangedListener

    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val userRepository = UserRepository(UserDatabase(this))
        val viewModelFactory = UserViewModelProviderFactory(application, userRepository)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

       // navController = findNavController(R.id.fragmentContainerView)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.setupWithNavController(navController)
//        binding.bottomNavView.setupWithNavController(navController)

        listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.usersDetailFragment || destination.id == R.id.likesFragment) {
                if (!binding.bottomNavView.isVisible){
                binding.bottomNavView.visibility = View.VISIBLE
                }
            } else {
                if (binding.bottomNavView.isVisible) {
                binding.bottomNavView.visibility = View.GONE
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }
}