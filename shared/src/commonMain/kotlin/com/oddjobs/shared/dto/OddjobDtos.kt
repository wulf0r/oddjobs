@file:OptIn(ExperimentalJsExport::class)

package com.oddjobs.shared.dto

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@Serializable
@JsExport
data class SaveOddjobRequest(
    var id : Long?,
    var name : String,
    var prompt : String
)

@Serializable
@JsExport
data class ListOddjobResponse(
    val items : Array<OddjobListItem>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ListOddjobResponse

        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        return items.contentHashCode()
    }
}

@Serializable
@JsExport
data class OddjobListItem(
    val name : String,
    val prompt : String)



