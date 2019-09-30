package com.glong.txtlib.entry.chars

/**
 * 英文字符
 * @author guolong
 * @since 2019/9/27
 */
class EnglishChar(c: Char) : TxtChar(c) {
    override var paddingStart: Float = PADDING / 2
    override var paddingEnd: Float = PADDING / 2

    companion object {
        var PADDING = 0f
    }
}