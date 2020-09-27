package dev.vitorramos.livequestions.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.unit.dp
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.model.Question
import dev.vitorramos.livequestions.ui.colorAnsweredQuestion
import dev.vitorramos.livequestions.ui.colorSecondaryText
import dev.vitorramos.livequestions.util.short

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuestionCard(
    questions: List<Question>,
    modifier: Modifier,
    chipStyling: ChipStyling,
    offset: Float,
    width: Int,
    index: Int,
    indexOffset: Int,
    questionsPerPage: Int,
    transitioning: Boolean,
) = Column(modifier.padding(0.dp, 16.dp), verticalArrangement = Arrangement.SpaceBetween) {
    val d = AmbientDensity.current.density
    val offsetOffset = (width * index)
    val px = offset + offsetOffset

    val middle = (px / d).dp
    val left = middle - (width / d).dp
    val right = middle + (width / d).dp

    val adjustedIndex = (index * questionsPerPage) + indexOffset

    val question = if (0 <= adjustedIndex && adjustedIndex < questions.size) {
        questions[adjustedIndex]
    } else {
        // TODO: loading
        Question()
    }
    val previousQuestion = if (adjustedIndex > questionsPerPage - 1) {
        questions[adjustedIndex - questionsPerPage]
    } else {
        // TODO: blank
        Question()
    }
    val nextQuestion = if (adjustedIndex < questions.lastIndex - questionsPerPage) {
        questions[adjustedIndex + questionsPerPage]
    } else {
        // TODO: blank
        Question()
    }

    Box {
        Text(
            previousQuestion.title,
            Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp).absoluteOffset(left)
        )
        Text(
            question.title,
            Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp).absoluteOffset(middle)
        )
        Text(
            nextQuestion.title,
            Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp).absoluteOffset(right)
        )
    }
    Column {
        AnimatedVisibility(
            visible = !transitioning,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LazyRowForIndexed(question.tags, Modifier.fillMaxWidth()) { index, tag ->
                Chip(
                    tag,
                    Modifier.padding(
                        if (index == 0) 16.dp else 4.dp,
                        0.dp,
                        if (index == question.tags.size - 1) 16.dp else 4.dp,
                        0.dp,
                    ),
                    chipStyling,
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        Divider(color = Color.LightGray)
        Box {
            if (previousQuestion.isAnswered != null) {
                Text(
                    AmbientContext.current.getString(if (previousQuestion.isAnswered) R.string.answered else R.string.not_answered_yet),
                    Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp).absoluteOffset(left),
                    if (previousQuestion.isAnswered) colorAnsweredQuestion else colorSecondaryText,
                )
            }
            if (question.isAnswered != null) {
                Text(
                    AmbientContext.current.getString(if (question.isAnswered) R.string.answered else R.string.not_answered_yet),
                    Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp).absoluteOffset(middle),
                    if (question.isAnswered) colorAnsweredQuestion else colorSecondaryText,
                )
            }
            if (nextQuestion.isAnswered != null) {
                Text(
                    AmbientContext.current.getString(if (nextQuestion.isAnswered) R.string.answered else R.string.not_answered_yet),
                    Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp).absoluteOffset(right),
                    if (nextQuestion.isAnswered) colorAnsweredQuestion else colorSecondaryText,
                )
            }
        }
        Divider(color = Color.LightGray)

        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth().padding(16.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Box {
                    Text(previousQuestion.date?.short() ?: "", Modifier.absoluteOffset(left))
                    Text(question.date?.short() ?: "", Modifier.absoluteOffset(middle))
                    Text(nextQuestion.date?.short() ?: "", Modifier.absoluteOffset(right))
                }
                Box {
                    Text(previousQuestion.user, Modifier.absoluteOffset(left))
                    Text(question.user, Modifier.absoluteOffset(middle))
                    Text(nextQuestion.user, Modifier.absoluteOffset(right))
                }
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Text(
                            previousQuestion.votes?.toString() ?: "",
                            Modifier.absoluteOffset(left)
                        )
                        Text(question.votes?.toString() ?: "", Modifier.absoluteOffset(middle))
                        Text(nextQuestion.votes?.toString() ?: "", Modifier.absoluteOffset(right))
                    }
                    Text(
                        AmbientContext.current.getString(R.string.votes),
                        color = colorSecondaryText
                    )
                }
                Spacer(Modifier.size(16.dp, 0.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Text(
                            previousQuestion.answers?.toString() ?: "",
                            Modifier.absoluteOffset(left)
                        )
                        Text(question.answers?.toString() ?: "", Modifier.absoluteOffset(middle))
                        Text(nextQuestion.answers?.toString() ?: "", Modifier.absoluteOffset(right))
                    }
                    Text(
                        AmbientContext.current.getString(R.string.answers),
                        color = colorSecondaryText
                    )
                }
            }
        }
    }
}
