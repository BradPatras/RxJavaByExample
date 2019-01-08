package io.github.mcasper3.rxjavabyexample.example1

data class UserUiModel(
    val fullName: String,
    val initials: String,
    val address: String,
    val phoneNumber: String?,
    val email: String?
)

data class CompanyUiModel(
    val name: String,
    val address: String,
    val phoneNumber: String?
)
