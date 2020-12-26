package dev.vitorramos.livequestions.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Question(
    val answers: Int,
    val date: Date,
    val isAnswered: Boolean,
    val tags: List<String>,
    val title: String,
    val user: String,
    val votes: Int,
) {
    internal constructor(questionItem: QuestionItem) : this(
        questionItem.answer_count,
        Date(questionItem.creation_date * 1000),
        questionItem.is_answered,
        questionItem.tags,
        questionItem.title,
        questionItem.owner.display_name,
        questionItem.score,
    )
}

internal data class QuestionsResponse(@SerializedName("items") val items: List<QuestionItem?>)

internal data class QuestionItem(
    @SerializedName("answer_count") val answer_count: Int,
    @SerializedName("creation_date") val creation_date: Long,
    @SerializedName("is_answered") val is_answered: Boolean,
    @SerializedName("score") val score: Int,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("title") val title: String,
    @SerializedName("owner") val owner: Owner,
)

internal data class Owner(
    @SerializedName("display_name") val display_name: String,
)
