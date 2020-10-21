package com.mikolove.allmightworkout.business.domain.model

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class BodyGroupFactory
@Inject
constructor()
{
    //For testing purpose
    fun getBodyGroup() : BodyGroup{
        return BodyGroup.values()[Random.nextInt(BodyGroup.values().size)]
    }

    //For testing purpose
    fun getListOfBodyGroup(numberOfBodyGroup : Int) : List<BodyGroup>{

        val listOfBodyGroup : ArrayList<BodyGroup> = ArrayList()
        for (i in 0 until numberOfBodyGroup){
            listOfBodyGroup.add(
                getBodyGroup()
            )
        }

        return listOfBodyGroup
    }
}