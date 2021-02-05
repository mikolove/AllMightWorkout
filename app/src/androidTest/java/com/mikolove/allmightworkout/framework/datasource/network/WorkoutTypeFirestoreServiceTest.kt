package com.mikolove.allmightworkout.framework.datasource.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.data.WorkoutTypeDataFactory
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutTypeFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.implementation.WorkoutTypeFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.mappers.BodyPartNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutTypeNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.BODYPART_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.WORKOUT_TYPE_COLLECTION
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@UninstallModules(ProductionModule::class)
@HiltAndroidTest
class WorkoutTypeFirestoreServiceTest : BaseTest() {

    //System in test
    private lateinit var workoutTypeFirestoreService : WorkoutTypeFirestoreService

    //Init hilt and dependencies
    @Inject
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    @Inject
    lateinit var workoutTypeNetworkMapper : WorkoutTypeNetworkMapper

    @Inject
    lateinit var bodyPartNetworkMapper: BodyPartNetworkMapper

    @Inject
    lateinit var workoutTypeDataFactory: WorkoutTypeDataFactory

    @Before
    fun init(){

        injectTest()
        signIn()
        insertWorkoutTypesData()

        workoutTypeFirestoreService = WorkoutTypeFirestoreServiceImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            workoutTypeNetworkMapper = workoutTypeNetworkMapper,
            bodyPartNetworkMapper = bodyPartNetworkMapper
        )

    }

    //Confirm By Search
    @Test
    fun getWorkoutTypes_CBS() = runBlocking{

        val workoutTypes = workoutTypeFirestoreService.getWorkoutTypes()
        //Should have 7 workoutTypes
        assertTrue { workoutTypes.size == 7}
        //Abs should have 4 bodyParts
        assertTrue { workoutTypes[0].bodyParts?.size == 4 }

    }

    override fun injectTest() {
        hiltRule.inject()
    }

    private fun signIn() = runBlocking {
        firebaseAuth.signInWithEmailAndPassword(
            FirestoreAuth.FIRESTORE_LOGIN,
            FirestoreAuth.FIRESTORE_PASSWORD
        ).await()
    }

    private fun insertWorkoutTypesData() = runBlocking{

        val workoutTypeList = workoutTypeDataFactory.produceListOfWorkoutTypes()

        for(workoutType in workoutTypeList){

            val workoutTypeEntity = workoutTypeNetworkMapper.mapToEntity(workoutType)
            firestore
                .collection(WORKOUT_TYPE_COLLECTION)
                .document(workoutTypeEntity.idWorkoutType)
                .set(workoutTypeEntity)
                .await()

            workoutType.bodyParts?.forEach { bodyPart ->

                val bodyPartEntity = bodyPartNetworkMapper.mapToEntity(bodyPart)
                firestore
                    .collection(WORKOUT_TYPE_COLLECTION)
                    .document(workoutType.idWorkoutType)
                    .collection(BODYPART_COLLECTION)
                    .document(bodyPartEntity.idBodyPart)
                    .set(bodyPartEntity)
                    .await()
            }
        }
    }
}