package com.mikolove.core.domain.exercise

import com.mikolove.core.domain.bodypart.BodyPart
import java.time.ZonedDateTime
import java.util.UUID

/*
 Peu etre
 CECI NE PEU RESTER COMME CA. LES SETS SONT LIE AU EXERCISE ET AU WORKOUT POUR POUVOIR CREER DES EXO INDEPENDANT ET PAS RE MULTIPLIE
RESULTAT ce modele n est plus correct faut modifié ca
*/

data class Exercise(
    var idExercise: String = UUID.randomUUID().toString(),
    var name: String,
    //var sets: List<ExerciseSet> = listOf(),
    var bodyParts: List<BodyPart> = listOf(),
    var exerciseType: ExerciseType,
    var isActive: Boolean = true,
    var startedAt: ZonedDateTime? = null,
    var endedAt: ZonedDateTime? = null,
    var createdAt: ZonedDateTime = ZonedDateTime.now(),
    var updatedAt: ZonedDateTime = ZonedDateTime.now()) {


    fun start(){
        startedAt = ZonedDateTime.now()
    }

    fun stop(){
        endedAt = ZonedDateTime.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exercise

        if (idExercise != other.idExercise) return false
        if (name != other.name) return false
        //if (sets != other.sets) return false
        if (bodyParts != other.bodyParts) return false
        if (exerciseType != other.exerciseType) return false
        if (isActive != other.isActive) return false
        if (startedAt != other.startedAt) return false
        if (endedAt != other.endedAt) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idExercise.hashCode()
        result = 31 * result + name.hashCode()
        //result = 31 * result + sets.hashCode()
        result = 31 * result + (bodyParts.hashCode())
        result = 31 * result + exerciseType.hashCode()
        result = 31 * result + isActive.hashCode()
        result = 31 * result + (startedAt?.hashCode() ?: 0)
        result = 31 * result + (endedAt?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        return result
    }
}

