package com.shadi777.todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shadi777.todoapp.data_sources.models.Priority
import com.shadi777.todoapp.data_sources.models.TodoItem

/**
 * Class that is used to store data of a single task.
 * It is used to save tasks to database.
 *
 * @constructor creates single task
 */
@Entity
data class TodoItemEntity(
    @PrimaryKey @ColumnInfo("id") val id: String,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("importance") val importance: Priority,
    @ColumnInfo("deadline") val deadline: Long? = null,
    @ColumnInfo("done") var done: Boolean = false,
    @ColumnInfo("color") val color: String?,
    @ColumnInfo("createdAt") val createdAt: Long,
    @ColumnInfo("changedAt") val changedAt: Long,
    @ColumnInfo("lastUpdatedBy") val lastUpdatedBy: String
) {
    /**
     * Convert this entity to task that is used as a model
     * @return converted element
     */
    fun toDomainModel(): TodoItem {
        return TodoItem(
            id = id,
            text = text,
            priority = importance,
            deadlineDate = deadline,
            isDone = done,
            color = color,
            createDate = createdAt,
            changeDate = changedAt,
            lastUpdatedBy = lastUpdatedBy
        )
    }
}
