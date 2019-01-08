package io.github.mcasper3.rxjavabyexample

import android.os.NetworkOnMainThreadException
import com.nhaarman.mockitokotlin2.verify
import io.github.mcasper3.rxjavabyexample.example1.CompanyDbModel
import io.github.mcasper3.rxjavabyexample.example1.DataSource
import io.github.mcasper3.rxjavabyexample.example1.Example1
import io.github.mcasper3.rxjavabyexample.example1.LocationDbModel
import io.github.mcasper3.rxjavabyexample.example1.UserDbModel
import io.github.mcasper3.rxjavabyexample.example1.UserUiModel
import io.reactivex.Observable
import org.junit.Test

class Example1Test {

    @Test
    fun getListOfUsers_returnsCorrectlyConvertedUsers() {
        val dataSource = createDataSource(
            listOf(
                UserDbModel(
                    "Test",
                    "Tester",
                    LocationDbModel("New York", "NY", "USA"),
                    "555-5555-5555",
                    "test.tester@test.test"
                ),
                UserDbModel(
                    "Stephen",
                    "Doe",
                    LocationDbModel("San Diego", "CA", "USA"),
                    "555-123-4567",
                    "youthoughtthiswasreal@test.test"
                )
            )
        )

        Example1(dataSource).getListOfUsers()
            .test()
            .assertValue(
                listOf(
                    UserUiModel(
                        "Test Tester",
                        "TT",
                        "New York, NY, USA",
                        "555-555-5555",
                        "test.tester@test.test"
                    ),
                    UserUiModel(
                        "Stephen Doe",
                        "SD",
                        "San Diego, CA, USA",
                        "555-123-4567",
                        "youthoughtthiswasreal@test.test"
                    )
                )
            )

        verify(dataSource).getUsers()
    }

    @Test
    fun getListOfUsersInCity_returnsCorrectlyConvertedUsersBasedOnCity() {
        val dataSource = createDataSource(
            listOf(
                UserDbModel(
                    "Test",
                    "Tester",
                    LocationDbModel("New York", "NY", "USA"),
                    "555-5555-5555",
                    "test.tester@test.test"
                ),
                UserDbModel(
                    "Stephen",
                    "Doe",
                    LocationDbModel("San Diego", "CA", "USA"),
                    "555-123-4567",
                    "youthoughtthiswasreal@test.test"
                ),
                UserDbModel(
                    "New",
                    "Person",
                    LocationDbModel("New York", "NY", "USA"),
                    "555-213-3243",
                    "testing@test.test"
                ),
                UserDbModel(
                    "Steve",
                    "Stevenson",
                    LocationDbModel("New York", "CA", "USA"),
                    "555-343-2351",
                    "something@test.test"
                )
            )
        )

        Example1(dataSource)
            .getListOfUsersInCity("New York")
            .test()
            .assertValue(
                listOf(
                    UserUiModel(
                        "Test Tester",
                        "TT",
                        "New York, NY, USA",
                        "555-555-5555",
                        "test.tester@test.test"
                    ),
                    UserUiModel(
                        "New Person",
                        "NP",
                        "New York, NY, USA",
                        "555-213-3243",
                        "testing@test.test"
                    ),
                    UserUiModel(
                        "Steve Evenson",
                        "SE",
                        "New York, CA, USA",
                        "555-343-2351",
                        "something@test.test"
                    )
                )
            )
        verify(dataSource).getUsers()
    }

    @Test
    fun getListOfUsersInStateOrderedByLastName_returnsCorrectListOfUsers() {
        val dataSource = createDataSource(
            listOf(
                UserDbModel(
                    "Test",
                    "Tester",
                    LocationDbModel("New York", "NY", "USA"),
                    "555-5555-5555",
                    "test.tester@test.test"
                ),
                UserDbModel(
                    "Stephen",
                    "Doe",
                    LocationDbModel("San Diego", "CA", "USA"),
                    "555-123-4567",
                    "youthoughtthiswasreal@test.test"
                ),
                UserDbModel(
                    "New",
                    "Person",
                    LocationDbModel("New York", "NY", "USA"),
                    "555-213-3243",
                    "testing@test.test"
                ),
                UserDbModel(
                    "Steve",
                    "Stevenson",
                    LocationDbModel("New York", "NY", "USA"),
                    "555-343-2351",
                    "something@test.test"
                ),
                UserDbModel(
                    "Randy",
                    "Aarons",
                    LocationDbModel("San Diego", "NY", "USA"),
                    "555-123-4567",
                    "youthoughtthiswasreal@test.test"
                ),
                UserDbModel(
                    "Some",
                    "Doels",
                    LocationDbModel("New York", "NY", "USA"),
                    "555-213-3243",
                    "testing@test.test"
                ),
                UserDbModel(
                    "Steve",
                    "Stevens",
                    LocationDbModel("New York", "NY", "USA"),
                    "555-343-2351",
                    "something@test.test"
                ),
                UserDbModel(
                    "Randy",
                    "Jokes",
                    LocationDbModel("San Diego", "NE", "USA"),
                    "555-123-4567",
                    "youthoughtthiswasreal@test.test"
                ),
                UserDbModel(
                    "Some",
                    "Someone",
                    LocationDbModel("New York", "PA", "USA"),
                    "555-213-3243",
                    "testing@test.test"
                )
            )
        )

        Example1(dataSource)
            .getListOfUsersInStateOrderedByLastName("NY")
            .test()
            .assertValue(
                listOf(
                    UserUiModel(
                        "Randy Aarons",
                        "RA",
                        "San Diego, NY, USA",
                        "555-123-4567",
                        "youthoughtthiswasreal@test.test"
                    ),
                    UserUiModel(
                        "Some Doels",
                        "SD",
                        "New York, NY, USA",
                        "555-213-3243",
                        "testing@test.test"
                    ),
                    UserUiModel(
                        "New Person",
                        "NP",
                        "New York, NY, USA",
                        "555-213-3243",
                        "testing@test.test"
                    ),
                    UserUiModel(
                        "Steve, Stevens",
                        "SS",
                        "New York, NY, USA",
                        "555-343-2351",
                        "something@test.test"
                    ),
                    UserUiModel(
                        "Steve, Stevenson",
                        "SS",
                        "New York, NY, USA",
                        "555-343-2351",
                        "something@test.test"
                    )
                )
            )

        verify(dataSource).getUsers()
    }

    private fun createDataSource(
        usersToReturn: List<UserDbModel> = throw IllegalAccessError(),
        companiesToReturn: List<CompanyDbModel> = throw IllegalAccessError()
    ): DataSource {
        return object : DataSource {
            override fun getUsers(): Observable<List<UserDbModel>> {
                crashIfMainThread()
                return Observable.just(usersToReturn)
            }

            override fun getGetCompanies(): Observable<List<CompanyDbModel>> {
                crashIfMainThread()
                return Observable.just(companiesToReturn)
            }


            private fun crashIfMainThread() {
                if ("main" == Thread.currentThread().name) throw NetworkOnMainThreadException()
            }
        }
    }
}
