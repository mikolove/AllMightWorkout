package com.mikolove.core.domain.util


interface EntityMapper<Entity, DomainModel> {

    fun mapFromEntity(entity : Entity) : DomainModel

    fun mapToEntity(domainModel : DomainModel) : Entity

    fun entityListToDomainList(entities: List<Entity>): List<DomainModel> {
        val domains : ArrayList<DomainModel> = ArrayList()
        for(entity in entities){
            domains.add(mapFromEntity(entity))
        }
        return domains
    }

    fun domainListToEntityList(domains: List<DomainModel>): List<Entity>{
        val entities : ArrayList<Entity> = ArrayList()
        for(domain in domains){
            entities.add(mapToEntity(domain))
        }
        return entities
    }
}