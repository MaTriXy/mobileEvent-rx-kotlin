package com.tikalk.mobileevent.mobileevent.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri


fun Context.getClipboardManager() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

fun Context.copyTextToClipboard(value: String) {
    getClipboardManager().primaryClip = ClipData.newPlainText("text", value)
}

fun Context.copyUriToClipboard(uri: Uri) {
    getClipboardManager().primaryClip = ClipData.newUri(contentResolver, "uri", uri)
}

fun Context.getTextFromClipboard(): CharSequence {
    val clipData = getClipboardManager().primaryClip
    if (clipData != null && clipData.itemCount > 0) {
        return clipData.getItemAt(0).coerceToText(this)
    }

    return ""
}

fun Context.getUriFromClipboard(): Uri? {
    val clipData = getClipboardManager().primaryClip
    if (clipData != null && clipData.itemCount > 0) {
        return clipData.getItemAt(0).uri
    }

    return null
}