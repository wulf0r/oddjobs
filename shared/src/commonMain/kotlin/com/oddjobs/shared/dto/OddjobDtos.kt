@file:OptIn(ExperimentalJsExport::class)

package com.oddjobs.shared.dto

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@Serializable
@JsExport
data class CreateOddjobRequest(
    var name : String,
    var prompt : String
)


