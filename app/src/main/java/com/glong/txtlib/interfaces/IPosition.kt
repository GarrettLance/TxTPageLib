package com.glong.txtlib.interfaces

/**
 * @author guolong
 * @since 2019/9/29
 */
interface IPosition {
    /**
     * 是否是行第一个字符
     */
    var inLineFirst: Boolean

    /**
     * 是否是行最后一个字符
     */
    var inLineLast: Boolean

    /**
     * 是否是段第一个字符
     */
    var inParagraphFirst: Boolean

    /**
     * 是否是段最后一个字符
     */
    var inParagraphLast: Boolean
}