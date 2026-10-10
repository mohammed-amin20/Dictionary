package com.mohammed.dictionary.feature.my_dictionary.data.util

import java.text.Normalizer

/*
 * Shared helpers to clean up third-party API text. MyMemory and dictionary
 * providers return HTML fragments, HTML entities, Unicode presentation forms
 * and stray reference artifacts that must not leak into the UI.
 */

private val HTML_TAG = Regex("<[^>]*>")
private val HTML_ENTITY = Regex("&(?:#x?[0-9a-fA-F]+|[a-zA-Z]+);")
private val WHITESPACE = Regex("\\s+")

private val NAMED_ENTITIES = mapOf(
    "amp" to "&",
    "lt" to "<",
    "gt" to ">",
    "quot" to "\"",
    "apos" to "'",
    "nbsp" to " ",
    "hellip" to "\u2026",
    "rsquo" to "\u2019",
    "lsquo" to "\u2018",
    "ldquo" to "\u201C",
    "rdquo" to "\u201D",
    "mdash" to "\u2014",
    "ndash" to "\u2013"
)

/** Folds Arabic presentation forms (U+FB50..U+FEFF) and other compatibility glyphs to standard text. */
fun String.normalizeUnicode(): String = Normalizer.normalize(this, Normalizer.Form.NFKC)

/** Decodes the HTML entities commonly returned by the dictionary/translation providers. */
fun String.decodeHtmlEntities(): String {
    if (!contains('&')) return this
    return HTML_ENTITY.replace(this) { match ->
        val entity = match.value
        when {
            entity.startsWith("&#x", ignoreCase = true) ->
                entity.drop(3).dropLast(1).toIntOrNull(radix = 16)
                    ?.let { codePoint -> String(Character.toChars(codePoint)) }
                    ?: entity

            entity.startsWith("&#") ->
                entity.drop(2).dropLast(1).toIntOrNull()
                    ?.let { codePoint -> String(Character.toChars(codePoint)) }
                    ?: entity

            else -> NAMED_ENTITIES[entity.substring(1, entity.length - 1)] ?: entity
        }
    }
}

/** Removes HTML tags and collapses whitespace. */
fun String.stripHtml(): String = HTML_TAG.replace(this, " ")

fun String.collapseWhitespace(): String = WHITESPACE.replace(this, " ").trim()

/** Generic cleanup for provider-supplied definitions and examples. */
fun String.cleanText(): String =
    this.normalizeUnicode().stripHtml().decodeHtmlEntities().collapseWhitespace()

/**
 * Cleanup tailored to machine translation output: also drops the trailing
 * "/source-word" reference artifacts MyMemory sometimes appends.
 */
fun String.cleanTranslatedText(query: String): String {
    var text = this.cleanText()
    val artifact = "/" + query.trim()
    if (artifact.length > 1 && text.endsWith(artifact, ignoreCase = true)) {
        text = text.dropLast(artifact.length).trim()
    }
    return text
}
