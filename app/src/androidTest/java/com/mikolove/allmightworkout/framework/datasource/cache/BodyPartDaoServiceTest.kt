package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.BodyPartFactory
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.model.WorkoutTypeFactory
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.BodyPartDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutTypeDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.BodyPartDao
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.BodyPartDaoServiceImpl
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.BodyPartCacheMapper
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
1. a_insertBodyPart_CBS
2. b_updateBodyPart_CBS
3. c_removeBodyPart_CBS
4. d_getBodyPartsByWorkoutType_CBS
5. e_getBodyPartById_CBS
6. f_getTotalBodyPartsByWorkoutType_CBS
7. g_getTotalBodyParts_CBS
8. h_getBodyParts_CBS
9. i_getBodyPartOrderByNameDESC_CBS
9. i_getBodyPartOrderByNameASC_CBS
10. j_getBodyPartByName_CBS

 */


@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BodyPartDaoServiceTest : BaseTest(){

    private lateinit var bodyPartDaoService : BodyPartDaoService

    @Inject
    lateinit var workoutTypeDaoService : WorkoutTypeDaoService

    @Inject
    lateinit var bodyPartDao : BodyPartDao

    @Inject
    lateinit var bodyPartCacheMapper: BodyPartCacheMapper

    @Inject
    lateinit var workoutTypeFactory : WorkoutTypeFactory

    @Inject
    lateinit var bodyPartFactory : BodyPartFactory

    @Inject
    lateinit var workoutTypeWithBodyPartCacheMapper: WorkoutTypeWithBodyPartCacheMapper

    @Before
    fun init(){
        injectTest()

        bodyPartDaoService = BodyPartDaoServiceImpl(
            bodyPartDao,
            bodyPartCacheMapper
        )

    }

    override fun injectTest() {
        hiltRule.inject()
    }

    //    1. a_insertBodyPart_CBS
    @Test
    fun a_insertBodyPart_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        workoutType.bodyParts = listOf(bodyPart)

        workoutTypeDaoService.insertWorkoutType(workoutType)

        val isInserted = bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)

        assertEquals(isInserted,1)

        val searchResult = bodyPartDaoService.getBodyPartById(bodyPart.idBodyPart)

        assertEquals(searchResult,bodyPart)

    }

    //    2. b_updateBodyPart_CBS
    @Test
    fun b_updateBodyPart_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        workoutType.bodyParts = listOf(bodyPart)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)

        val updatedBodyPart = bodyPartFactory.createBodyPart(
            bodyPart.idBodyPart,
            "abs updated"
        )

        val isUpdated = bodyPartDaoService.updateBodyPart(updatedBodyPart.idBodyPart,updatedBodyPart.name)

        assertEquals(isUpdated,1)

        val searchResult = bodyPartDaoService.getBodyPartById(updatedBodyPart.idBodyPart)

        assertEquals(searchResult,updatedBodyPart)
    }

    //    3. c_removeBodyPart_CBS
    @Test
    fun c_removeBodyPart_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        workoutType.bodyParts = listOf(bodyPart)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)

        val isDeleted = bodyPartDaoService.removeBodyPart(bodyPart.idBodyPart)

        assertEquals(isDeleted,1)

        val searchResult = bodyPartDaoService.getBodyPartById(bodyPart.idBodyPart)

        assertEquals(searchResult,null)
    }

    //    4. d_getBodyPartsByWorkoutType_CBS
    @Test
    fun d_getBodyPartsByWorkoutType_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        workoutType.bodyParts = listOf(bodyPart)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)

        val searchResult = bodyPartDaoService.getBodyPartsByWorkoutType(workoutType.idWorkoutType)

        assertTrue { searchResult?.contains(bodyPart) == true }

        val workoutTypeWithNoBodyPart = createWorkoutTypeTwo()

        workoutTypeDaoService.insertWorkoutType(workoutTypeWithNoBodyPart)

        val searchNoResult = bodyPartDaoService.getBodyPartsByWorkoutType(workoutTypeWithNoBodyPart.idWorkoutType)

        assertTrue { searchNoResult.isEmpty() }

    }

    //    5. e_getBodyPartById_CBS
    @Test
    fun e_getBodyPartById_CBS() = runBlocking {

        val searchResult = bodyPartDaoService.getBodyPartById("idNull")

        assertEquals(searchResult,null)

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        workoutType.bodyParts = listOf(bodyPart)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)

        val searchResultNotNull = bodyPartDaoService.getBodyPartById(bodyPart.idBodyPart)

        assertEquals(searchResultNotNull,bodyPart)

    }

    //    6. f_getTotalBodyPartsByWorkoutType_CBS
    @Test
    fun f_getTotalBodyPartsByWorkoutType_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        workoutType.bodyParts = listOf(bodyPart)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)

        val totalBodyPartByWorkoutType = bodyPartDaoService.getTotalBodyPartsByWorkoutType(workoutType.idWorkoutType)

        assertEquals(totalBodyPartByWorkoutType,1)
    }

    //    7. g_getTotalBodyParts_CBS
    @Test
    fun g_getTotalBodyParts_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        val workoutTypeTwo = createWorkoutType()
        workoutTypeTwo.idWorkoutType = workoutTypeTwo.idWorkoutType+"Two"
        val bodyPartTwo = createBodyPart()
        bodyPartTwo.idBodyPart = bodyPartTwo.idBodyPart+"Two"

        workoutType.bodyParts = listOf(bodyPart)
        workoutTypeTwo.bodyParts = listOf(bodyPartTwo)

        val listOfWorkoutType = listOf(workoutType,workoutTypeTwo)

        listOfWorkoutType.forEach { wkT ->
            workoutTypeDaoService.insertWorkoutType(wkT)
            wkT.bodyParts?.forEach {
                bodyPartDaoService.insertBodyPart(it,wkT.idWorkoutType)
            }
        }

        val totalBodyParts = bodyPartDaoService.getTotalBodyParts()

        assertEquals(totalBodyParts,2)
    }

    //    8. h_getBodyParts_CBS
    @Test
    fun h_getBodyParts_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        val workoutTypeTwo = createWorkoutType()
        workoutTypeTwo.idWorkoutType = workoutTypeTwo.idWorkoutType+"Two"
        val bodyPartTwo = createBodyPart()
        bodyPartTwo.idBodyPart = bodyPartTwo.idBodyPart+"Two"

        workoutType.bodyParts = listOf(bodyPart)
        workoutTypeTwo.bodyParts = listOf(bodyPartTwo)

        val listOfWorkoutType = listOf(workoutType,workoutTypeTwo)

        listOfWorkoutType.forEach { wkT ->
            workoutTypeDaoService.insertWorkoutType(wkT)
            wkT.bodyParts?.forEach {
                bodyPartDaoService.insertBodyPart(it,wkT.idWorkoutType)
            }
        }

        val searchResult = bodyPartDaoService.getBodyParts()

        assertTrue { searchResult.contains(bodyPart) }
        assertTrue { searchResult.contains(bodyPartTwo) }
    }

    //    9. i_getBodyPartOrderByNameDESC_CBS
    @Test
    fun i_getBodyPartOrderByNameDESC_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        val bodyPartTwo = createBodyPartTwo()
        workoutType.bodyParts = listOf(bodyPart,bodyPartTwo)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)
        bodyPartDaoService.insertBodyPart(bodyPartTwo,workoutType.idWorkoutType)

        val sortedBodyParts = workoutType.bodyParts?.sortedByDescending { it.name } ?: ArrayList()

        val searchResult = bodyPartDaoService.getBodyPartOrderByNameDESC("",1)

        assertTrue {  searchResult.zip(sortedBodyParts).all { (x,y) -> x == y }  }

    }

    //    9. i_getBodyPartOrderByNameASC_CBS
    @Test
    fun i_getBodyPartOrderByNameASC_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        val bodyPartTwo = createBodyPartTwo()
        workoutType.bodyParts = listOf(bodyPart,bodyPartTwo)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)
        bodyPartDaoService.insertBodyPart(bodyPartTwo,workoutType.idWorkoutType)

        val sortedBodyParts = workoutType.bodyParts?.sortedBy { it.name } ?: ArrayList()

        val searchResult = bodyPartDaoService.getBodyPartOrderByNameASC("",1)

        assertTrue {  searchResult.zip(sortedBodyParts).all { (x,y) -> x == y }  }

    }

    //    10. j_getBodyPartByName_CBS
    @Test
    fun j_getBodyPartByName_CBS() = runBlocking {

        val workoutType = createWorkoutType()
        val bodyPart = createBodyPart()
        val bodyPartTwo = createBodyPartTwo()
        workoutType.bodyParts = listOf(bodyPart,bodyPartTwo)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)
        bodyPartDaoService.insertBodyPart(bodyPartTwo,workoutType.idWorkoutType)

        val searchResult = bodyPartDaoService.getBodyPartOrderByNameDESC("lower",1)
        assertTrue { searchResult.contains(bodyPartTwo) }
        assertTrue { !searchResult.contains(bodyPart) }

    }

    private fun createBodyPart() : BodyPart = bodyPartFactory.createBodyPart(
        idBodyPart = "abs_upper",
        name = "upper abs"
    )

    private fun createBodyPartTwo() : BodyPart = bodyPartFactory.createBodyPart(
        idBodyPart = "abs_lower",
        name = "lower abs"
    )
    private fun createWorkoutType() : WorkoutType = workoutTypeFactory.createWorkoutType(
        idWorkoutType = "abs",
        name = "abs",
        bodyParts = null
    )

    private fun createWorkoutTypeTwo() : WorkoutType = workoutTypeFactory.createWorkoutType(
        idWorkoutType = "chest",
        name = "chest",
        bodyParts = null
    )
}