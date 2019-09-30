package com.glong.txtlib.entry.chars

/**
 * 自定义的Char类型
 * @author guolong
 * @since 2019/9/27
 */
class CustomChar(c: Char) : TxtChar(c) {
    override var paddingStart: Float = PADDING / 2
    override var paddingEnd: Float = PADDING / 2

    open fun isCustomChar(): Boolean {
        return when (char) {
            '/' -> true
            '-' -> true
            else -> false
        }
//        return false
    }

    companion object {
        var PADDING = 0f
    }
}