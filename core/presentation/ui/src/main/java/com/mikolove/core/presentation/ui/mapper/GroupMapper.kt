package com.mikolove.core.presentation.ui.mapper

import com.mikolove.core.domain.workout.Group
import com.mikolove.core.presentation.ui.model.GroupUi

fun Group.toGroupUi() : GroupUi {
    return GroupUi(
        idGroup = this.idGroup,
        name = this.name,
    )
}

fun GroupUi.toGroup() : Group {
    return Group(
        idGroup = this.idGroup,
        name = this.name,
    )
}