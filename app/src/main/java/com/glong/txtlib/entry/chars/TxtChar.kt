package com.glong.txtlib.entry.chars

import com.glong.txtlib.interfaces.IDraw
import com.glong.txtlib.interfaces.IPosition
import com.glong.txtlib.interfaces.ISelection
import com.glong.txtlib.utils.CharType
import com.glong.txtlib.utils.isCustomChar
import com.glong.txtlib.utils.isEnglish
import com.glong.txtlib.utils.isNumber

/**
 * 字符基类
 * @author guolong
 * @since 2019/9/27
 */
abstract class TxtChar(
    /**
     * 字符
     */
    var char: Char
) : IDraw, ISelection, IPosition {
    /**
     * 在当前章节中的索引
     */
    var indexInChapter: Int = 0

    // 是否是一组字符的第一个 ，比如"I Love You"中 'I','L','Y'都是一组字符中的最后一个
    internal var isCombinesStart: Boolean = false // 忽略行首
    // 是否是一组字符的最后一个 ，比如"I Love You"中 'I','e','u'都是一组字符中的最后一个
    internal var isCombinesEnd: Boolean = false // 忽略行末

    fun charType(): CharType {
        return when {
            this.char.isCustomChar() -> CharType.CUSTOM
            this.char.isNumber() -> CharType.NUMBER
            this.char.isEnglish() -> CharType.ENGLISH
            else -> CharType.CHINESE
        }
    }

    // ############### IDraw接口的实现
    // 字符在View中的位置
    override var x:Float = 0f

    // 字符宽度
    override var charWidth: Float = 0f

    // 主要用于draw的时候确定字符前后间距
    abstract override var paddingStart: Float
    abstract override var paddingEnd: Float

    // ################ ISelection 接口的实现
    override var isSelected: Boolean = false
    override var markStyles: MutableSet<Int>? = null

    // ################ IPosition 接口的实现
    override var inLineFirst: Boolean = false
    override var inLineLast: Boolean = false
    override var inParagraphFirst: Boolean = false
    override var inParagraphLast: Boolean = false
}