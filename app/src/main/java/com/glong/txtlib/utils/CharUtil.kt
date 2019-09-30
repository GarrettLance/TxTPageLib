package com.glong.txtlib.utils

import com.glong.txtlib.entry.chars.*
import java.util.regex.Pattern

/**
 * @author guolong
 * @since 2019/9/27
 */

/**
 * 是否是汉字
 */
fun Char.isChinese(): Boolean {
    val pattern = Pattern.compile("^[\u4e00-\u9fa5]+$")
    return pattern.matcher(this.toString()).matches()
}

/**
 * 是否是数字
 */
fun Char.isNumber(): Boolean {
    val pattern = Pattern.compile("^[-+]?[\\d]*$")
    return pattern.matcher(this.toString()).matches()
}

/**
 * 是否是英文
 */
fun Char.isEnglish(): Boolean {
    return this in 'a'..'z' || this in 'A'..'Z' || this == '.'
}

fun Char.isCustomChar(): Boolean {
    return CustomChar(this).isCustomChar()
}

/**
 * 根据字符类型，获取特定的类型对象
 */
fun Char.toSpecifiedChar(): TxtChar {
    return when {
        this.isCustomChar() -> CustomChar(this)
        this.isNumber() -> NumberChar(this)
        this.isEnglish() -> EnglishChar(this)
        else -> ChineseChar(this)
    }
}

fun Char.charType(): CharType {
    return when {
        this.isCustomChar() -> CharType.CUSTOM
        this.isNumber() -> CharType.NUMBER
        this.isEnglish() -> CharType.ENGLISH
        else -> CharType.CHINESE
    }
}