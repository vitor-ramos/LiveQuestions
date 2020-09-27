package dev.vitorramos.livequestions.util

import java.text.DateFormat
import java.util.*

fun Date.short(): String = DateFormat.getDateInstance(DateFormat.SHORT).format(this)
