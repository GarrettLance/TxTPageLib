package com.glong.txtlib.entry

import com.glong.txtlib.entry.chars.LinkChar

/**
 * @author guolong
 * @since 2019/9/30
 */
class TxtPage {
    val lines = mutableListOf<TxtLine>()

    internal var surpassLength: Float = 0f

    val size: Int
        get() = lines.size

    /**
     * 页索引
     */
    var pageIndex:Int = 0

    /**
     * 当前页所有字符数量
     */
    val charSize: Int
        get() {
            var result = 0
            lines.forEach { txtLine ->
                result += txtLine.chars.filter { it !is LinkChar }.size
            }
            return result
        }
}