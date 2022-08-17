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
}