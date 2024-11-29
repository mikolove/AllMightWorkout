package com.mikolove.core.presentation.ui.mapper

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.presentation.ui.model.BodyPartUi

fun BodyPart.toBodyPartUi() : BodyPartUi {
    return BodyPartUi(
        idBodyPart = this.idBodyPart,
        name = this.name
    )
}
