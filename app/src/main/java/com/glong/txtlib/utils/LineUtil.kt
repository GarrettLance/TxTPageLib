package com.glong.txtlib.utils

import com.glong.txtlib.entry.TxtLine
import com.glong.txtlib.entry.chars.TxtChar

/**
 * @author guolong
 * @since 2019/9/27
 */
fun TxtLine.last(): TxtChar {
    return this.chars.last()
}

fun TxtLine.lastOrNull(): TxtChar? {
    return this.chars.lastOrNull()
}

fun TxtLine.lastIndex(): Int {
    if (this.chars.isNullOrEmpty()) {
        throw NullPointerException("TxtLine is Empty")
    }
    return this.chars.lastIndex
}

fun TxtLine.lastIndexOrNull(): Int? {
    if (this.chars.isNullOrEmpty()) {
        return null
    }
    return this.chars.lastIndex
}