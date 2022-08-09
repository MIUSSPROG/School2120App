package com.example.school2120app.core.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest{

    @Test
    fun `empty username returns false`(){
        val result = RegistrationUtil.validateRegistrationInput("", "123", "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and correctly repeated password returns true`(){
        val result = RegistrationUtil.validateRegistrationInput("Nickolay", "123", "123")
        assertThat(result).isTrue()
    }

    @Test
    fun `username already exists return false`(){
        val result = RegistrationUtil.validateRegistrationInput("Misha", "123", "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `incorrectly confirmed password returns false`(){
        val result = RegistrationUtil.validateRegistrationInput("Misha", "123", "abc")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`(){
        val result = RegistrationUtil.validateRegistrationInput("Nickolay", "", "")
        assertThat(result).isFalse()
    }
}