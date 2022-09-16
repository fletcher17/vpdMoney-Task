package com.example.vpdmoneytask.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.example.vpdmoneytask.model.UserDetailsResponse
import com.example.vpdmoneytask.model.UserDetailsResponseItem
import com.example.vpdmoneytask.repository.UserRepository
import com.example.vpdmoneytask.util.Resource
import com.example.vpdmoneytask.util.UserApplication
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserViewModel(
    application: Application,
    val userRepository: UserRepository) : AndroidViewModel(application) {

    private val _userDetails: MutableLiveData<Resource<UserDetailsResponse>> = MutableLiveData()
    val userDetails: LiveData<Resource<UserDetailsResponse>> get() = _userDetails

    private val _singleUserItem: MutableLiveData<UserDetailsResponseItem> = MutableLiveData()
    val singleUserItem: LiveData<UserDetailsResponseItem> get() = _singleUserItem

    init {
        getUserDetails()
    }

    fun getUserDetails() {
        viewModelScope.launch {
            getUsers()
        }
    }

    suspend fun getUsers() {
        _userDetails.postValue(Resource.Loading())
        try {
            if (checkInternetConnection()) {
                val response = userRepository.getUserDetailsFromApi()
                _userDetails.postValue(handleUserDetailsResponse(response))
            } else {
                _userDetails.postValue(Resource.Error("Network Failure"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> _userDetails.postValue(Resource.Error("No Internet Connection"))
                else -> _userDetails.postValue(Resource.Error("Conversion Error"))
            }
        }


    }

    fun saveUser(user: UserDetailsResponseItem) = viewModelScope.launch {
        userRepository.update(user)
    }

    fun getSavedUser() = userRepository.getSavedDetails()

    fun deleteUser(user: UserDetailsResponseItem) = viewModelScope.launch {
        userRepository.deleteUser(user)
    }



    fun sendDetails(user: UserDetailsResponseItem) {
        _singleUserItem.postValue(user)
    }

    private fun handleUserDetailsResponse(response: Response<UserDetailsResponse>): Resource<UserDetailsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { userDetailsResult ->
                return Resource.Success(userDetailsResult)
            }
        }
        return Resource.Error(response.message())
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getApplication<UserApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }



}