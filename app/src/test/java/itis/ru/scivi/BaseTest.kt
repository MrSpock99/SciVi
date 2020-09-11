package itis.ru.scivi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.ClassRule
import org.junit.Rule

open class BaseTest {
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    // Test rule for making the RxJava to run synchronously in unit test
    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }
}