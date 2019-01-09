package io.github.mcasper3.rxjavabyexample

import android.os.NetworkOnMainThreadException
import com.nhaarman.mockitokotlin2.verify
import io.github.mcasper3.rxjavabyexample.example1.CompanyDbModel
import io.github.mcasper3.rxjavabyexample.example1.CompanyUiModel
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
                TEST_TESTER_DB,
                STEPHEN_DOE_DB
            )
        )

        Example1(dataSource).getListOfUsers()
            .test()
            .assertValue(
                listOf(
                    TEST_TESTER_UI,
                    STEPHEN_DOE_UI
                )
            )

        verify(dataSource).getUsers()
    }

    @Test
    fun getListOfUsersInCity_returnsCorrectlyConvertedUsersBasedOnCity() {
        val dataSource = createDataSource(
            listOf(
                TEST_TESTER_DB,
                STEPHEN_DOE_DB,
                NEW_PERSON_DB,
                STEVE_STEVENSON_DB
            )
        )

        Example1(dataSource)
            .getListOfUsersInCity("New York")
            .test()
            .assertValue(
                listOf(
                    TEST_TESTER_UI,
                    NEW_PERSON_UI,
                    STEVE_STEVENSON_UI
                )
            )
        verify(dataSource).getUsers()
    }

    @Test
    fun getListOfUsersInStateOrderedByLastName_returnsCorrectListOfUsers() {
        val dataSource = createDataSource(
            listOf(
                TEST_TESTER_DB,
                STEPHEN_DOE_DB,
                NEW_PERSON_DB,
                STEVE_STEVENSON_DB,
                RANDY_AARONS_DB,
                SOME_DOELS_DB,
                STEVE_STEVENS_DB,
                RANDY_JOKES_DB,
                SOME_SOMEONE_DB
            )
        )

        Example1(dataSource)
            .getListOfUsersInStateOrderedByLastName("NY")
            .test()
            .assertValue(
                listOf(
                    RANDY_AARONS_UI,
                    SOME_DOELS_UI,
                    NEW_PERSON_UI,
                    STEVE_STEVENS_UI,
                    STEVE_STEVENSON_UI
                )
            )

        verify(dataSource).getUsers()
    }

    @Test
    fun getListOfUsersAndCompaniesInState_returnsCorrectListOfUsersAndCompanies() {
        val dataSource = createDataSource(
            listOf(
                RANDY_AARONS_DB,
                STEPHEN_DOE_DB,
                NEW_PERSON_DB,
                STEVE_STEVENSON_DB,
                SOME_DOELS_DB,
                RANDY_JOKES_DB
            ),
            listOf(
                CompanyDbModel("Best Company", LocationDbModel("Best City", "NY", "USA"), "555-555-5555"),
                CompanyDbModel("Other Company", LocationDbModel("New York", "CA", "USA"), "555-355-5555"),
                CompanyDbModel("Fake Company", LocationDbModel("Testing", "CA", "USA"), "555-555-5513"),
                CompanyDbModel("Michaela Inc.", LocationDbModel("Las Vegas", "CA", "USA"), "555-816-5918"),
                CompanyDbModel("Da Bears", LocationDbModel("Philly", "PA", "USA"), "555-513-5555"),
                CompanyDbModel("Easy Life", LocationDbModel("County", "AK", "USA"), "555-513-5555")
            )
        )

        val users = listOf(
            STEPHEN_DOE_UI,
            STEVE_STEVENSON_UI
        )
        val cities = listOf(
            CompanyUiModel("Other Company", "New York, CA, USA", "555-355-5555"),
            CompanyUiModel("Fake Company", "Testing, CA, USA", "555-555-5513"),
            CompanyUiModel("Michaela Inc.", "Las Vegas, CA, USA", "555-816-5918")
        )
        val expected = users to cities

        Example1(dataSource)
            .getListOfUsersAndCompaniesInState("CA")
            .test()
            .assertValue(expected)

        verify(dataSource).getUsers()
        verify(dataSource).getGetCompanies()
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

    companion object {
        private val TEST_TESTER_DB = UserDbModel(
            "Test",
            "Tester",
            LocationDbModel("New York", "NY", "USA"),
            "555-5555-5555",
            "test.tester@test.test"
        )
        private val STEPHEN_DOE_DB = UserDbModel(
            "Stephen",
            "Doe",
            LocationDbModel("San Diego", "CA", "USA"),
            "555-123-4567",
            "youthoughtthiswasreal@test.test"
        )
        private val NEW_PERSON_DB = UserDbModel(
            "New",
            "Person",
            LocationDbModel("New York", "NY", "USA"),
            "555-213-3243",
            "testing@test.test"
        )
        private val STEVE_STEVENSON_DB = UserDbModel(
            "Steve",
            "Stevenson",
            LocationDbModel("New York", "CA", "USA"),
            "555-343-2351",
            "something@test.test"
        )
        private val RANDY_AARONS_DB = UserDbModel(
            "Randy",
            "Aarons",
            LocationDbModel("San Diego", "NY", "USA"),
            "555-123-4567",
            "youthoughtthiswasreal@test.test"
        )
        private val SOME_DOELS_DB = UserDbModel(
            "Some",
            "Doels",
            LocationDbModel("New York", "NY", "USA"),
            "555-213-3243",
            "testing@test.test"
        )
        private val STEVE_STEVENS_DB = UserDbModel(
            "Steve",
            "Stevens",
            LocationDbModel("New York", "NY", "USA"),
            "555-343-2351",
            "something@test.test"
        )
        private val RANDY_JOKES_DB = UserDbModel(
            "Randy",
            "Jokes",
            LocationDbModel("San Diego", "NE", "USA"),
            "555-123-4567",
            "youthoughtthiswasreal@test.test"
        )
        private val SOME_SOMEONE_DB = UserDbModel(
            "Some",
            "Someone",
            LocationDbModel("New York", "PA", "USA"),
            "555-213-3243",
            "testing@test.test"
        )

        private val TEST_TESTER_UI = UserUiModel(
            "Test Tester",
            "TT",
            "New York, NY, USA",
            "555-555-5555",
            "test.tester@test.test"
        )
        private val STEPHEN_DOE_UI = UserUiModel(
            "Stephen Doe",
            "SD",
            "San Diego, CA, USA",
            "555-123-4567",
            "youthoughtthiswasreal@test.test"
        )
        private val NEW_PERSON_UI = UserUiModel(
            "New Person",
            "NP",
            "New York, NY, USA",
            "555-213-3243",
            "testing@test.test"
        )
        private val STEVE_STEVENSON_UI = UserUiModel(
            "Steve Stevenson",
            "SE",
            "New York, CA, USA",
            "555-343-2351",
            "something@test.test"
        )
        private val RANDY_AARONS_UI = UserUiModel(
            "Randy Aarons",
            "RA",
            "San Diego, NY, USA",
            "555-123-4567",
            "youthoughtthiswasreal@test.test"
        )
        private val SOME_DOELS_UI = UserUiModel(
            "Some Doels",
            "SD",
            "New York, NY, USA",
            "555-213-3243",
            "testing@test.test"
        )
        private val STEVE_STEVENS_UI = UserUiModel(
            "Steve Stevens",
            "SS",
            "New York, NY, USA",
            "555-343-2351",
            "something@test.test"
        )
    }
}
