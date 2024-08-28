package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.bodypart.BodyPartFactory
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.workouttype.WorkoutTypeFactory
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.BodyPartDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutTypeDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutTypeDao
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.WorkoutTypeDaoServiceImpl
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutTypeCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutTypeWithBodyPartCacheMapper
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/*
1. a_insertWorkoutType_CBS
2. b_updateWorkoutType_CBS
3. c_removeWorkoutType_CBS
4. d_getWorkoutTypeBydBodyPartId_CBS
5. e_getWorkoutTypeById_CBS
6. f_getWorkoutTypes_CBS
7. g_getTotalWorkoutTypes_CBS
8. h_getWorkoutTypeOrderByNameDESC_CBS
9. i_getWorkoutTypeOrderByNameASC_CBS
10. j_getWorkoutTypeByName_CBS

 */

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class WorkoutTypeCacheServiceTest : BaseTest(){

    private lateinit var workoutTypeDaoService : WorkoutTypeDaoService

    @Inject
    lateinit var bodyPartDaoService : BodyPartDaoService

    @Inject
    lateinit var workoutTypeDao : WorkoutTypeDao

    @Inject
    lateinit var workoutTypeCacheMapper : WorkoutTypeCacheMapper

    @Inject
    lateinit var workoutTypeFactory : WorkoutTypeFactory

    @Inject
    lateinit var bodyPartFactory : BodyPartFactory

    @Inject
    lateinit var workoutTypeWithBodyPartCacheMapper: WorkoutTypeWithBodyPartCacheMapper

    @Before
    fun init(){
        injectTest()

        workoutTypeDaoService = WorkoutTypeDaoServiceImpl(
            workoutTypeDao = workoutTypeDao,
            workoutTypeCacheMapper = workoutTypeCacheMapper,
            workoutTypeWithBodyPartCacheMapper =workoutTypeWithBodyPartCacheMapper
        )

    }
    override fun injectTest() {
        hiltRule.inject()
    }

    //    1. a_insertWorkoutType_CBS
    @Test
    fun a_insertWorkoutType_CBS() = runBlocking {

        val workoutType = createWorkoutType()

        val isInserted = workoutTypeDaoService.insertWorkoutType(workoutType)

        assertEquals(isInserted,1)

        val searchResult = workoutTypeDaoService.getWorkoutTypeById(workoutType.idWorkoutType)

        assertEquals(workoutType,searchResult)
    }

    //    2. b_updateWorkoutType_CBS
    @Test
    fun b_updateWorkoutType_CBS() = runBlocking {

        val workoutType = createWorkoutType()

        workoutTypeDaoService.insertWorkoutType(workoutType)

        val searchResult = workoutTypeDaoService.getWorkoutTypeById(workoutType.idWorkoutType)

        val updatedWorkoutType = workoutTypeFactory.createWorkoutType(
            idWorkoutType = searchResult?.idWorkoutType,
            name = "abs_lower",
            bodyParts = searchResult?.bodyParts
        )

        val isUpdated = workoutTypeDaoService.updateWorkoutType(updatedWorkoutType.idWorkoutType,updatedWorkoutType.name)

        assertEquals(isUpdated, 1)

        val searchUpdatedResult = workoutTypeDaoService.getWorkoutTypeById(workoutType.idWorkoutType)

        assertEquals(searchUpdatedResult,updatedWorkoutType)

    }
    //    3. c_removeWorkoutType_CBS
    @Test
    fun c_removeWorkoutType_CBS() = runBlocking {

        val workoutType = createWorkoutType()

        workoutTypeDaoService.insertWorkoutType(workoutType)

        val isDeleted = workoutTypeDaoService.removeWorkoutType(workoutType.idWorkoutType)

        assertEquals(isDeleted,1)

        val searchResult = workoutTypeDaoService.getWorkoutTypeById(workoutType.idWorkoutType)

        assertEquals(searchResult,null)
    }

    //    4. d_getWorkoutTypeBydBodyPartId_CBS
    @Test
    fun d_getWorkoutTypeBydBodyPartId_CBS() = runBlocking {

        val bodyPart = createBodyPart()
        val workoutType = createWorkoutType()

        workoutType.bodyParts = listOf(bodyPart)

        workoutTypeDaoService.insertWorkoutType(workoutType)

        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)

        val searchResult = workoutTypeDaoService.getWorkoutTypeBydBodyPartId(bodyPart.idBodyPart)

        assertEquals(searchResult,workoutType)
    }

    //    5. e_getWorkoutTypeById_CBS
    @Test
    fun e_getWorkoutTypeById_CBS() = runBlocking {


        val workoutType = createWorkoutType()

        workoutTypeDaoService.insertWorkoutType(workoutType)


        val searchResult = workoutTypeDaoService.getWorkoutTypeById(workoutType.idWorkoutType)

        assertEquals(searchResult,workoutType)

    }

    //    6. f_getWorkoutTypes_CBS
    @Test
    fun f_getWorkoutTypes_CBS() = runBlocking {

        val workoutType = createWorkoutType()

        workoutTypeDaoService.insertWorkoutType(workoutType)

        val searchResult = workoutTypeDaoService.getWorkoutTypes()

        assertTrue { searchResult.contains(workoutType)}
    }

    //    7. g_getTotalWorkoutTypes_CBS
    @Test
    fun g_getTotalWorkoutTypes_CBS() = runBlocking {

        val workoutType = createWorkoutType()

        workoutTypeDaoService.insertWorkoutType(workoutType)

        val searchResult = workoutTypeDaoService.getTotalWorkoutTypes()

        assertEquals(searchResult,1)
    }

    //    8. h_getWorkoutTypeOrderByNameDESC_CBS
    @Test
    fun h_getWorkoutTypeOrderByNameDESC_CBS() = runBlocking {

        val workoutTypeOne = workoutTypeFactory.createWorkoutType(
            "abs",
            "abs",
            bodyParts = null
        )

        val workoutTypeTwo = workoutTypeFactory.createWorkoutType(
            "pecs",
            "pecs",
            bodyParts = null
        )
        val listOfWorkoutType = listOf(workoutTypeOne,workoutTypeTwo)

        listOfWorkoutType.forEach {
            workoutTypeDaoService.insertWorkoutType(it)
        }

        val sortedWorkoutTypes = listOfWorkoutType.sortedByDescending { it.name }

        val searchResult = workoutTypeDaoService.getWorkoutTypeOrderByNameDESC("",1)

        assertTrue { sortedWorkoutTypes.zip(searchResult).all{ (x,y) -> x == y } }
    }

    //    9. i_getWorkoutTypeOrderByNameASC_CBS
    @Test
    fun i_getWorkoutTypeOrderByNameASC_CBS() = runBlocking {

        val workoutTypeOne = workoutTypeFactory.createWorkoutType(
            "abs",
            "abs",
            bodyParts = null
        )

        val workoutTypeTwo = workoutTypeFactory.createWorkoutType(
            "pecs",
            "pecs",
            bodyParts = null
        )
        val listOfWorkoutType = listOf(workoutTypeOne,workoutTypeTwo)

        listOfWorkoutType.forEach {
            workoutTypeDaoService.insertWorkoutType(it)
        }

        val sortedWorkoutTypes = listOfWorkoutType.sortedBy { it.name }

        val searchResult = workoutTypeDaoService.getWorkoutTypeOrderByNameASC("",1)

        assertTrue { sortedWorkoutTypes.zip(searchResult).all{ (x,y) -> x == y } }

    }

    @Test
    fun j_getWorkoutTypeByName() = runBlocking {

        val workoutTypeOne = workoutTypeFactory.createWorkoutType(
            "abs",
            "abs",
            bodyParts = null
        )

        val workoutTypeTwo = workoutTypeFactory.createWorkoutType(
            "pecs",
            "pecs",
            bodyParts = null
        )
        val listOfWorkoutType = listOf(workoutTypeOne,workoutTypeTwo)

        listOfWorkoutType.forEach {
            workoutTypeDaoService.insertWorkoutType(it)
        }

        val searchResult = workoutTypeDaoService.getWorkoutTypeOrderByNameASC("pecs",1)

        assertTrue { searchResult.contains(workoutTypeTwo) }


    }

    private fun createBodyPart() : BodyPart = bodyPartFactory.createBodyPart(
        idBodyPart = "abs_upper",
        name = "upper abs"
    )

    private fun createWorkoutType() : WorkoutType = workoutTypeFactory.createWorkoutType(
        idWorkoutType = "abs",
        name = "abs",
        bodyParts = null
    )
}