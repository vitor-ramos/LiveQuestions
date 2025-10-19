package dev.vitorramos.livequestions.model

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("name") val name: String,
)

data class TagsResponse(
    @SerializedName("items") val items: List<Tag?>,
)
