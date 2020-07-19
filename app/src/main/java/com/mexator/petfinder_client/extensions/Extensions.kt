package com.mexator.petfinder_client.extensions

import android.content.Context
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat

fun Any.getTag(): String {
    return this.javaClass.simpleName
}

/**
 * Create a formatted CharSequence from a string resource containing arguments and HTML formatting
 *
 * The string resource must be wrapped in a CDATA section so that the HTML formatting is conserved.
 *
 * Example of an HTML formatted string resource:
 * <string name="html_formatted"><![CDATA[ bold text: <B>%1$s</B> ]]></string>
 */
fun Context.getText(@StringRes id: Int, vararg args: Any?): CharSequence {
    val text = String.format(getString(id), *args)
    return HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
}
