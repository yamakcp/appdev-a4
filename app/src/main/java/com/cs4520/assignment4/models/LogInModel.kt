package com.cs4520.assignment4.models

class LogInModel {
    fun validateCredentials(
        username: String, password: String,
    ): Boolean {
        return (username == "admin") && (password == "admin")
    }
}
