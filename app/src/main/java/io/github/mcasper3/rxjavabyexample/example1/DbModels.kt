package io.github.mcasper3.rxjavabyexample.example1

data class UserDbModel(
    val firstName: String,
    val lastName: String,
    val location: LocationDbModel,
    val phoneNumber: String?,
    val email: String?
)

data class LocationDbModel(
    val city: String,
    val state: String,
    val country: String
)

data class CompanyDbModel(
    val name: String,
    val location: LocationDbModel,
    val phoneNumber: String?
)
