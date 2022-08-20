package com.example.school2120app.core.util

import app.cash.turbine.test
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class HelperMethodsTest{

    private lateinit var helperMethods: HelperMethods
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        helperMethods = HelperMethods(testDispatchers)
    }

    @Test
    fun `testing countdown`() = runBlocking{
        helperMethods.getCountDownFlow().test{
            for (i in 5 downTo 0){
                testDispatchers.testDispatcher.advanceTimeBy(1000L)
                val emission = awaitItem()
                assertThat(emission).isEqualTo(i)
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test date in future`() = runBlocking {
        val future = "25-08-2022"
        val expected = HelperMethods.isDateToCome(future)
        val actual = true
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `test date in past`() = runBlocking {
        val future = "10-08-2022"
        val expected = HelperMethods.isDateToCome(future)
        val actual = false
        assertThat(expected).isEqualTo(actual)
    }
}