package com.shadi777.todoapp.data_sources.models

import com.google.gson.annotations.SerializedName
import com.shadi777.todoapp.database.TodoItemEntity
import java.io.Serializable

enum class Priority {
    @SerializedName("basic") Default,
    @SerializedName("low") Low,
    @SerializedName("important") High
}

/**
 * Class that is used to store all information about task
 *
 * @constructor Creates single task
 */
data class TodoItem (
    @SerializedName("id")         var id: String,
    @SerializedName("text")       var text: String,
    @SerializedName("importance") var priority: Priority,
    @SerializedName("done")       var isDone: Boolean,
    @SerializedName("color")      var color: String? = null,
    @SerializedName("created_at") var createDate: Long,
    @SerializedName("changed_at") var changeDate: Long,
    @SerializedName("deadline")   var deadlineDate: Long? = null,
    @SerializedName("last_updated_by") var lastUpdatedBy: String = "SampleDevice"
) : Serializable {

    /**
     * Convert this task to entity that can be stored in database
     * @return converted element
     */
    fun toDatabaseEntity(): TodoItemEntity {
        return TodoItemEntity(
            id = id,
            text = text,
            importance = priority,
            deadline = deadlineDate,
            done = isDone,
            color = color,
            createdAt = createDate,
            changedAt = changeDate,
            lastUpdatedBy = lastUpdatedBy
        )
    }
}
