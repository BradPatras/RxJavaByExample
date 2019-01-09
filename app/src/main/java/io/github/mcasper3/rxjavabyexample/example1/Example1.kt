package io.github.mcasper3.rxjavabyexample.example1

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class Example1(private val dataSource: DataSource) {

    fun getListOfUsers(): Observable<List<UserUiModel>> = dataSource.getUsers()
        .map { users -> users.map { user -> mapUser(user) } }

    fun getListOfUsersInCity(city: String): Observable<List<UserUiModel>> = dataSource.getUsers()
        .flatMapIterable { it }
        .filter { it.location.city == city }
        .map { mapUser(it) }
        .toList()
        .toObservable()

    fun getListOfUsersInStateOrderedByLastName(state: String): Observable<List<UserUiModel>> = dataSource.getUsers()
        .flatMapIterable { it }
        .filter { it.location.state == state }
        .sorted { user1, user2 -> user1.lastName.compareTo(user2.lastName) }
        .map { mapUser(it) }
        .toList()
        .toObservable()

    fun getListOfUsersAndCompaniesInState(state: String): Observable<Pair<List<UserUiModel>, List<CompanyUiModel>>> {
        val usersSingle = dataSource.getUsers()
            .flatMapIterable { it }
            .filter { it.location.state == state }
            .map { mapUser(it) }
            .toList()
        val companiesSingle = dataSource.getCompanies()
            .flatMapIterable { it }
            .filter { it.location.state == state }
            .map { company -> CompanyUiModel(company.name, mapLocation(company.location), company.phoneNumber) }
            .toList()
        return Single.zip(
            usersSingle,
            companiesSingle,
            BiFunction { users: List<UserUiModel>, companies: List<CompanyUiModel> -> users to companies }
        ).toObservable()
    }

    private fun mapLocation(location: LocationDbModel): String {
        return with(location) { "$city, $state, $country" }
    }

    private fun mapUser(user: UserDbModel): UserUiModel {
        val address = mapLocation(user.location)
        val initials = "${user.firstName.first()}${user.lastName.first()}"
        return UserUiModel("${user.firstName} ${user.lastName}", initials, address, user.phoneNumber, user.email)
    }
}
