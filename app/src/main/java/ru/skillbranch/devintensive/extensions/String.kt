package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {
    val result = this.trim()
    return if (result.length <= length + 1) result
    else "${result.take(length + 1).trimEnd()}..."
}

fun String.stripHtml(): String =
    this
        .replace("""<.*?>""".toRegex(), "")
        .replace("""&(#\d+?|\w+?);""".toRegex(), "")
        .split(""" +""".toRegex()).joinToString(" ")