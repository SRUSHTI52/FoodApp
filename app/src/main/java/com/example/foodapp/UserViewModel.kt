package com.example.foodapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao: UserDao = CartDatabase.getDatabase(application).userDao()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun login(username: String) {
        Log.d("userviewmodel", "called")
        viewModelScope.launch {
            val loggedInUser = userDao.login(username)
            if (loggedInUser != null) {
                Log.d("userviewmodel", "username : ${loggedInUser.username}")
            }
            _user.postValue(loggedInUser)
        }
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userDao.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userDao.deleteUser(user)
        }
    }

    fun logout() {
        _user.postValue(null)
    }
}
