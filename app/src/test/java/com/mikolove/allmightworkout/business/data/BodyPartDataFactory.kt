package com.mikolove.allmightworkout.business.data

import com.mikolove.core.domain.bodypart.BodyPart

class BodyPartDataFactory(testClassLoader: ClassLoader,
filename: String
) : JsonDataFactory<BodyPart>(filename,testClassLoader) {

    override fun produceHashMapOfT(tList: List<BodyPart>): HashMap<String, BodyPart> {
        val map = HashMap<String, BodyPart>()
        for(bp in tList){
            map.put(bp.idBodyPart, bp)
        }
        return map
    }
}