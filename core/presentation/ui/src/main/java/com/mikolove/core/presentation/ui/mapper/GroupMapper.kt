package com.mikolove.core.presentation.ui.mapper

import com.mikolove.core.domain.workout.Group
import com.mikolove.core.presentation.ui.model.GroupUi

fun Group.toGroupUi() : GroupUi {
    return GroupUi(
        idGroup = this.idGroup,
        name = this.name,
    )
}

fun List<GroupUi>.toIds() : List<String> {
    return if (this.none { it.selected }) {
        this.mapIndexed { _, group ->
            group.idGroup
        }
    } else {
        this.filter { it.selected }.mapIndexed { _, group ->
            group.idGroup
        }
    }
}


fun GroupUi.toGroup() : Group {
    return Group(
        idGroup = this.idGroup,
        name = this.name,
    )
}