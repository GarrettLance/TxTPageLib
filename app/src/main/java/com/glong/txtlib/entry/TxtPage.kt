package com.glong.txtlib.entry

import com.glong.txtlib.entry.TxtLine

/**
 * @author guolong
 * @since 2019/9/30
 */
class TxtPage {
    val lines = mutableListOf<TxtLine>()

    internal var surpassLength: Float = 0f

    val size: Int
        get() = lines.size
}