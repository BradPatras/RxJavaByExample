package io.github.mcasper3.rxjavabyexample.example2

import io.reactivex.Observable

interface DataSource {

    fun getUsersInGroup(id: Long): Observable<List<User>>

    fun getNextFiveNumbers(): List<Int>
}
