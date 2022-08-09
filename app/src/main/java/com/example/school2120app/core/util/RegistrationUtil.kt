package com.example.school2120app.core.util

object RegistrationUtil {

    private val existingUsers = listOf("Nickolay", "Misha")

    fun validateRegistrationInput(
        username: String,
        password: String,
        confirmedPassword: String
    ): Boolean{
        if (username.isEmpty() || password.isEmpty()){
            return false
        }
        if (username in existingUsers){
            return false
        }
        if (password != confirmedPassword){
            return false
        }
        if (password.count { it.isDigit() } < 2){
            return false
        }
        return true
    }

}