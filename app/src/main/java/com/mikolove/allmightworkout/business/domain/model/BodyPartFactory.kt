package com.mikolove.allmightworkout.business.domain.model

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class BodyPartFactory
@Inject
constructor()
{
    //For testing purpose
    fun getBodyPart() : BodyPart{
        return BodyPart.values()[Random.nextInt(BodyGroup.values().size)]
    }

    //For testing purpose
    fun getListOfBodyPart(numberOfBodyPart : Int) : List<BodyPart>{

        val listOfBodyPart : ArrayList<BodyPart> = ArrayList()
        for (i in 0 until numberOfBodyPart){
            listOfBodyPart.add(
                getBodyPart()
            )
        }

        return listOfBodyPart
    }
}