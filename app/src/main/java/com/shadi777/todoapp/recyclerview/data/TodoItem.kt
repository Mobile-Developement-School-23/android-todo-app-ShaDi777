package com.shadi777.todoapp.recyclerview.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.*
import java.io.Serializable
import java.util.Date

enum class Priority {
    @SerializedName("basic") Default,
    @SerializedName("low") Low,
    @SerializedName("important") High
}

data class TodoItem (
    @SerializedName("id")         var id: String,
    @SerializedName("text")       var text: String,
    @SerializedName("importance") var priority: Priority,
    @SerializedName("done")       var is_done: Boolean,
    @SerializedName("created_at") var create_date: Long,
    @SerializedName("changed_at") var change_date: Long,
    @SerializedName("deadline")   var deadline_date: Long? = null, // optional
    @SerializedName("last_updated_by") var device_id: String = "SampleDevice"
) : Serializable


data class TodoItemContainer (
    val element: TodoItem
)