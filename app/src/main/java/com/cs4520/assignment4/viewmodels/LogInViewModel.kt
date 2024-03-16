package com.cs4520.assignment4.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs4520.assignment4.models.LogInModel

class LogInViewModel : ViewModel() {
    private val model: LogInModel = LogInModel()

    private val successLoginEvent = MutableLiveData<Unit>()
    val loginSuccess get() = successLoginEvent

    private val errorMssgData = MutableLiveData<String>()
    val errorMessage get() = errorMssgData

    fun tryLogin(
        username: String,
        password: String,
    ) {

        if (model.validateCredentials(username, password)) {
            successLoginEvent.postValue(Unit)

            //wrong username or password
        } else {
            errorMssgData.postValue("Invalid username and/or password." +
                    "                          \nTry Again.")
        }
    }
}
