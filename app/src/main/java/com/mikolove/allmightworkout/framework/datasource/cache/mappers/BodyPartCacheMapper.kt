package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.BodyPartCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class BodyPartCacheMapper
@Inject
constructor(
    private val roomDateUtil: RoomDateUtil
): EntityMapper<BodyPartCacheEntity,BodyPart>{

    override fun mapFromEntity(entity: BodyPartCacheEntity): BodyPart {
        return BodyPart(
            idBodyPart = entity.idBodyPart,
            name = entity.name
        )
    }

    override fun mapToEntity(domainModel: BodyPart): BodyPartCacheEntity {
        return BodyPartCacheEntity(
            idBodyPart = domainModel.idBodyPart,
            name = domainModel.name,
            idWorkoutType = null
        )
    }

    override fun entityListToDomainList(entities: List<BodyPartCacheEntity>): List<BodyPart> {
        val list : ArrayList<BodyPart> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<BodyPart>): List<BodyPartCacheEntity> {
        val entities : ArrayList<BodyPartCacheEntity> = ArrayList()
        for(bodyPart in domains){
            entities.add(mapToEntity(bodyPart))
        }
        return entities
    }
}