package com.mikolove.allmightworkout.business.domain.util

interface EntityMapper<Entity, DomainModel> {

    fun mapFromEntity(entity : Entity) : DomainModel

    fun mapToEntity(domainModel : DomainModel) : Entity

    fun entityListToDomainList(entities: List<Entity>): List<DomainModel>

    fun domainListToEntityList(domains: List<DomainModel>): List<Entity>
}