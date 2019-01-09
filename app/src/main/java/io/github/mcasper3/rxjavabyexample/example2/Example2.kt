package io.github.mcasper3.rxjavabyexample.example2

import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

class Example2(private val dataSource: DataSource) {

    fun getAggregateMaxAgeOfUserGroups(userGroupIds: List<Long>): Observable<Int> = Observable.fromIterable(userGroupIds)
        .flatMap { dataSource.getUsersInGroup(it).map { users -> users.maxBy { user -> user.age }?.age ?: 0 } }
        .reduce { total: Int, nextAge: Int -> total + nextAge }
        .toObservable()

    // Every 5 seconds, get the next 5 numbers and add them to the previous result and output that result
    fun getAggregate(intervalScheduler: Scheduler): Observable<Int> = Observable.interval(5, TimeUnit.SECONDS, intervalScheduler)
        .map { dataSource.getNextFiveNumbers().sum() }
        .scan { previousTotal, newValue -> previousTotal + newValue }
}
