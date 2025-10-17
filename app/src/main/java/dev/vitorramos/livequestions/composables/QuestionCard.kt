package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.model.Question
import dev.vitorramos.livequestions.ui.colorAnsweredQuestion
import dev.vitorramos.livequestions.ui.colorSecondaryText
import dev.vitorramos.livequestions.util.short

@Composable
fun QuestionCard(
    question: Question,
    chipStyling: ChipStyling,
) = Column {
    Spacer(Modifier.height(8.dp))
    Text(question.title, Modifier.padding(16.dp, 0.dp))
    Spacer(Modifier.height(8.dp))
    LazyRow(Modifier.fillMaxWidth()) {
        itemsIndexed(question.tags) { index, tag ->
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
    HorizontalDivider(color = Color.LightGray)
    Text(
        LocalContext.current.getString(if (question.isAnswered) R.string.answered else R.string.not_answered_yet),
        Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
        if (question.isAnswered) colorAnsweredQuestion else colorSecondaryText,
    )
    HorizontalDivider(color = Color.LightGray)
    Spacer(Modifier.height(8.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(question.date.short())
            Text(question.user)
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(question.votes.toString())
                Text(
                    LocalContext.current.getString(R.string.votes),
                    color = colorSecondaryText
                )
            }
            Spacer(Modifier.size(16.dp, 0.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(question.answers.toString())
                Text(
                    LocalContext.current.getString(R.string.answers),
                    color = colorSecondaryText
                )
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    HorizontalDivider(color = Color.Black)
}
