package io.github.mcasper3.rxjavabyexample.example2

import io.reactivex.Observable
import io.reactivex.Scheduler

class Example2(private val dataSource: DataSource) {

    fun getAggregateMaxAgeOfUserGroups(userGroupIds: List<Long>): Observable<Int> = TODO()

    // Every 5 seconds, get the next 5 numbers and add them to the previous result and output that result
    fun getAggregate(intervalScheduler: Scheduler): Observable<Int> = TODO()
}
