package dev.vitorramos.livequestions.model

import com.google.gson.annotations.SerializedName

internal data class Tag(@SerializedName("name") val name: String)

internal data class TagsResponse(@SerializedName("items") val items: List<Tag?>)
