package com.mikolove.allmightworkout.framework.datasource.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.UserFactory
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.UserFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.implementation.UserFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.UserNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.*
import org.junit.runners.MethodSorters
import java.util.*
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserFirestoreServiceTest : BaseTest(){

    private lateinit var userFirestoreService : UserFirestoreService

    //Init hilt and dependencies
    @Inject
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    @Inject
    lateinit var userFactory : UserFactory

    @Inject
    lateinit var userNetworkMapper: UserNetworkMapper

    @Inject
    lateinit var workoutNetworkMapper : WorkoutNetworkMapper

    @Inject
    lateinit var exerciseNetworkMapper : ExerciseNetworkMapper

    @Inject
    lateinit var dateUtil : DateUtil

    override  fun injectTest() {
        hiltRule.inject()
    }

    private fun signIn() = runBlocking {
        firebaseAuth.signInWithEmailAndPassword(
            FirestoreAuth.FIRESTORE_LOGIN,
            FirestoreAuth.FIRESTORE_PASSWORD
        ).await()
    }

    private fun signOut() = firebaseAuth.signOut()


    @Before
    fun setup(){

        injectTest()
        signOut()
        signIn()

        userFirestoreService = UserFirestoreServiceImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            userNetworkMapper = userNetworkMapper,
            workoutNetworkMapper = workoutNetworkMapper,
            exerciseNetworkMapper = exerciseNetworkMapper,
            dateUtil = dateUtil
        )

    }

    @After
    fun closing(){
        signOut()
    }

    @Test
    fun a_insertUser_CBS() = runBlocking {

        val userId = firebaseAuth.currentUser?.uid
        if (userId!= null) {
            val user = userFactory.createUser(
                idUser = userId,
                email = "test@test.test",
                name = null
            )

            userFirestoreService.insertUser(user)

            val searchUser = userFirestoreService.getUser(user.idUser)

            assertEquals(user,searchUser)

        }
    }

    @Test
    fun b_updateName_CBS() = runBlocking {

        val userId = firebaseAuth.currentUser?.uid
        if (userId!= null) {

            val user = userFactory.createUser(
                idUser = userId,
                email = "test@test.test",
                name = null
            )

            userFirestoreService.insertUser(user)

            val searchUser = userFirestoreService.getUser(user.idUser)

            assertEquals(searchUser?.name,null)

            delay(1000)

            val newName = "newName"

            userFirestoreService.updateName(newName,userId)

            val searchUpdatedUser = userFirestoreService.getUser(user.idUser)

            assertEquals(searchUpdatedUser?.name,newName)
            if (searchUpdatedUser != null && searchUser !=null) {
                assert(dateUtil.convertStringDateToDate(searchUpdatedUser.updatedAt).after(dateUtil.convertStringDateToDate(searchUser.updatedAt)))
            }else{
                Assert.fail("One of the user is null")
            }
        }
    }


    @Test
    fun c_getUser() = runBlocking {
        val userId = firebaseAuth.currentUser?.uid
        if(userId != null){
            val user = userFirestoreService.getUser(userId)
            assertEquals(user?.idUser,userId)

        }
    }
}
