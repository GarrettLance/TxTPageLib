package com.glong.txtlib.utils

import com.glong.txtlib.entry.Mark

/**
 * @author guolong
 * @since 2019/9/29
 */
/**
 * @return styleIndex 集合
 */
fun List<Mark>.match(charIndex: Int): MutableSet<Int> {
    val markStyles = mutableSetOf<Int>()
    this.forEach {
        if (charIndex in it.charStartIndexInChapter..it.charEndIndexInChapter) {
            markStyles.add(it.styleIndex)
        }
    }
    return markStyles
}