package io.github.mcasper3.rxjavabyexample.example1

import io.reactivex.Observable

class Example1(private val dataSource: DataSource) {

    fun getListOfUsers(): Observable<List<UserUiModel>> = TODO()

    fun getListOfUsersInCity(city: String): Observable<List<UserUiModel>> = TODO()

    fun getListOfUsersInStateOrderedByLastName(state: String): Observable<List<UserUiModel>> = TODO()

    fun getListOfUsersAndCompaniesInState(state: String): Observable<Pair<List<UserUiModel>, List<CompanyUiModel>>> = TODO()
}
