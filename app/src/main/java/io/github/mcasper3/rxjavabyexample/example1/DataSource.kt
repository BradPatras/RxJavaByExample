package io.github.mcasper3.rxjavabyexample.example1

import io.reactivex.Observable

interface DataSource {

    fun getUsers(): Observable<List<UserDbModel>>

    fun getGetCompanies(): Observable<List<CompanyDbModel>>
}
