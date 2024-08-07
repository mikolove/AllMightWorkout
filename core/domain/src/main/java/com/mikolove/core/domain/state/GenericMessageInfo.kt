package com.mikolove.core.domain.state

class GenericMessageInfo
private constructor(builder: Builder){

    // required
    val id: String
    val title: String
    val uiComponentType: UIComponentType
    val messageType : MessageType

    // optional
    val onDismiss: (() -> Unit)?
    val description: String?
    val positiveAction: PositiveAction?
    val negativeAction: NegativeAction?

    init {
        if(builder.id == null){
            throw Exception("GenericMessageInfo id cannot be null.")
        }
        if(builder.title == null){
            throw Exception("GenericMessageInfo title cannot be null.")
        }
        if(builder.uiComponentType == null){
            throw Exception("GenericMessageInfo uiComponentType cannot be null.")
        }
        if(builder.messageType == null){
            throw Exception("GenericMessageInfo messageType cannot be null.")
        }
        this.id = builder.id!!
        this.title = builder.title!!
        this.uiComponentType = builder.uiComponentType!!
        this.messageType = builder.messageType!!
        this.onDismiss = builder.onDismiss
        this.description = builder.description
        this.positiveAction = builder.positiveAction
        this.negativeAction = builder.negativeAction
    }

    class Builder {

        var id: String? = null
            private set

        var title: String? = null
            private set

        var onDismiss: (() -> Unit)? = null
            private set

        var uiComponentType: UIComponentType? = null
            private set

        var messageType : MessageType? = null
            private set

        var description: String? = null
            private set

        var positiveAction: PositiveAction? = null
            private set

        var negativeAction: NegativeAction? = null
            private set

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun onDismiss(onDismiss: () -> Unit): Builder {
            this.onDismiss = onDismiss
            return this
        }

        fun uiComponentType(
            uiComponentType: UIComponentType
        ) : Builder {
            this.uiComponentType = uiComponentType
            return this
        }

        fun messageType(
            messageType : MessageType
        ): Builder {
            this.messageType = messageType
            return this
        }

        fun description(
            description: String
        ): Builder {
            this.description = description
            return this
        }

        fun positive(
            positiveAction: PositiveAction?,
        ) : Builder {
            this.positiveAction = positiveAction
            return this
        }

        fun negative(
            negativeAction: NegativeAction
        ) : Builder {
            this.negativeAction = negativeAction
            return this
        }

        fun build() = GenericMessageInfo(this)
    }
}

fun GenericMessageInfo.doesMessageAlreadyExistInQueue(queue: Queue<GenericMessageInfo>): Boolean{
    for(item in queue.items){
        if (item.id == this.id){
            return true
        }
    }
    return false
}

data class PositiveAction(
    val positiveBtnTxt: String,
    val onPositiveAction: () -> Unit,
)

data class NegativeAction(
    val negativeBtnTxt: String,
    val onNegativeAction: () -> Unit,
)