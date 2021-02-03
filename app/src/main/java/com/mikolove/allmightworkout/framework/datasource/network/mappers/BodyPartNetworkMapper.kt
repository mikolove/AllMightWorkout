package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.BodyPartNetworkEntity

class BodyPartNetworkMapper : EntityMapper<BodyPartNetworkEntity,BodyPart>{

    override fun mapFromEntity(entity: BodyPartNetworkEntity): BodyPart {
        return BodyPart(
            idBodyPart = entity.idBodyPart,
            name = entity.name
        )
    }

    override fun mapToEntity(domainModel: BodyPart): BodyPartNetworkEntity {
        return BodyPartNetworkEntity(
            idBodyPart = domainModel.idBodyPart,
            name = domainModel.name
        )
    }

    override fun entityListToDomainList(entities: List<BodyPartNetworkEntity>): List<BodyPart> {
        val list : ArrayList<BodyPart> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<BodyPart>): List<BodyPartNetworkEntity> {
        val entities : ArrayList<BodyPartNetworkEntity> = ArrayList()
        for(bodyPart in domains){
            entities.add(mapToEntity(bodyPart))
        }
        return entities
    }
}