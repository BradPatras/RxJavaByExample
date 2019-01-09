package io.github.mcasper3.rxjavabyexample

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.mcasper3.rxjavabyexample.example2.DataSource
import io.github.mcasper3.rxjavabyexample.example2.Example2
import io.github.mcasper3.rxjavabyexample.example2.User
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit

class Example2Test {

    private val numbers = (1..60).chunked(5)
    private var current = 0

    @Test
    fun getAggregateMaxAgeOfUserGroups_returnsCorrectAge() {
        val dataSource = createDataSource()
        val example2 = Example2(dataSource)

        example2.getAggregateMaxAgeOfUserGroups(listOf(USER_GROUP_1, USER_GROUP_2, USER_GROUP_3, USER_GROUP_4, USER_GROUP_5))
            .test()
            .assertValue(244)

        verify(dataSource).getUsersInGroup(USER_GROUP_1)
        verify(dataSource).getUsersInGroup(USER_GROUP_2)
        verify(dataSource).getUsersInGroup(USER_GROUP_3)
        verify(dataSource).getUsersInGroup(USER_GROUP_4)
        verify(dataSource).getUsersInGroup(USER_GROUP_5)
    }

    @Test
    fun getAggregate() {
        val dataSource = createDataSource()
        val example2 = Example2(dataSource)

        val testScheduler = TestScheduler()
        val testObserver = TestObserver.create<Int>()
        example2.getAggregate(testScheduler)
            .subscribe(testObserver)

        whenever(dataSource.getNextFiveNumbers())
            .thenReturn(numbers[current++])
        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS)
        testObserver.assertValues(15)
        whenever(dataSource.getNextFiveNumbers())
            .thenReturn(numbers[current++])
        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS)
        testObserver.assertValues(15, 55)
        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)
        testObserver.assertValues(15, 55)
        whenever(dataSource.getNextFiveNumbers())
            .thenReturn(numbers[current++])
        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS)
        testObserver.assertValues(15, 55, 120)

        verify(dataSource, times(3)).getNextFiveNumbers()
    }

    private fun createDataSource(): DataSource {
        return mock<DataSource>().apply {
            whenever(getUsersInGroup(USER_GROUP_1))
                .thenReturn(
                    Observable.just(
                        listOf(
                            User(73),
                            User(76),
                            User(32),
                            User(63)
                        )
                    )
                )
            whenever(getUsersInGroup(USER_GROUP_2))
                .thenReturn(
                    Observable.just(
                        listOf(
                            User(89),
                            User(32),
                            User(55),
                            User(43)
                        )
                    )
                )
            whenever(getUsersInGroup(USER_GROUP_3))
                .thenReturn(
                    Observable.just(
                        listOf(
                            User(17),
                            User(28),
                            User(28),
                            User(23)
                        )
                    )
                )
            whenever(getUsersInGroup(USER_GROUP_4))
                .thenReturn(
                    Observable.just(
                        listOf(
                            User(51),
                            User(42),
                            User(37),
                            User(15)
                        )
                    )
                )
            whenever(getUsersInGroup(USER_GROUP_5))
                .thenReturn(Observable.just(emptyList()))
        }
    }

    companion object {
        private const val USER_GROUP_1 = 1L
        private const val USER_GROUP_2 = 2L
        private const val USER_GROUP_3 = 3L
        private const val USER_GROUP_4 = 4L
        private const val USER_GROUP_5 = 5L
    }
}
