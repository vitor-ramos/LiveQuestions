package dev.vitorramos.livequestions

import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

inline fun <T> List<T>.moveToFirst(predicate: (T) -> Boolean): List<T> =
    firstOrNull(predicate)?.let { first ->
        mutableListOf<T>(first).also { it.addAll(filterNot(predicate)) }
    } ?: this

fun SharedPreferences.getString(key: String): String? = getString(key, null)

fun SharedPreferences.string(key: String, defValue: String): String = getString(key, defValue)!!

@Composable
fun getString(@StringRes id: Int) = LocalContext.current.getString(id)

fun convert(input: Float, minInput: Float, maxInput: Float, minOutput: Float, maxOutput: Float) =
    input / (maxInput - minInput) * (maxOutput - minOutput)

fun String.labeledHexToLong() =
    "$this${if (length == 4) substring(1, length) else ""}".replace("#", "FF").toLong(16)
