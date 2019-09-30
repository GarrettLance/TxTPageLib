package com.glong.txtlib.utils

import android.graphics.Paint
import com.glong.txtlib.entry.Mark
import com.glong.txtlib.entry.TxtLine
import com.glong.txtlib.entry.chars.LinkChar
import com.glong.txtlib.entry.chars.TxtChar

/**
 * @author guolong
 * @since 2019/9/27
 */
object TxtBreakUtil {
    var lineSymbol: MutableSet<String> = mutableSetOf("<br><br>", "<br>", "</p>", "\n", "/n")//换行符
    var indentationSymbol: String = "　　"// 缩进符
//    var combines: MutableSet<CharType> = mutableSetOf(CharType.ENGLISH, CharType.NUMBER)//需要整合的数据类型

    /**
     * 获取行数据集合
     * @param src 源字符数组
     * @param measureWidth 一行最大宽度
     * @param paint 画字符的目标画笔
     * @return 行数据集合
     */
    @JvmStatic
    fun breakTxt(
        src: String, measureWidth: Float,
        padding: IntArray,//View的padding
        paint: Paint, marks: List<Mark>? = null
    ): MutableList<TxtLine>? {
        val result: MutableList<TxtLine> = mutableListOf()
        val srcSize = src.length
        var dealSize = 0 //已经处理过的字符数量

        while (dealSize < srcSize) {
            val isParagraphStart = result.isEmpty() || result.last().isParagraphEnd
            val txtLine = getTxtLine(
                isParagraphStart,
                dealSize,
                src.substring(dealSize),
                measureWidth,
                padding,
                paint,
                marks
            )
            result.add(txtLine)
            dealSize += txtLine.validSize + txtLine.lineSymbol.length
        }
        return result
    }

    /**
     * @param isParagraphStart 是否是段首
     * @param dealSize 已经测量的字符数量
     * @param src 源字符串
     * @param measureWidth 目标View的宽度
     * @param padding 目标View的padding
     * @param paint 画笔
     * @param marks 被标记的字符
     * @return 行数据
     */
    @JvmStatic
    private fun getTxtLine(
        isParagraphStart: Boolean,
        dealSize: Int,
        src: String,
        measureWidth: Float,
        padding: IntArray,
        paint: Paint,
        marks: List<Mark>?
    ): TxtLine {
        val txtLine = TxtLine().apply {
            this.isParagraphStart = isParagraphStart
            if (isParagraphStart) {
                this.lineIndentationSymbol = indentationSymbol
            }
        }
        val usableWidth =
            measureWidth - padding[0] - padding[2] - paint.measureText(txtLine.lineIndentationSymbol)

        var charIndex = 0
        var width = 0f// 已经测量的宽度
        while (charIndex < src.length) {
            val c = src[charIndex]

            // 换行逻辑 start---------
            val lineSymbol = newlineSymbol(charIndex, src)
            if (!lineSymbol.isNullOrEmpty()) {
                txtLine.isParagraphEnd = true
                txtLine.lineSymbol = lineSymbol
                break
            }
            // 换行逻辑 end-----------


            val charWidth = paint.measureText(c.toString())
            val txtChar = c.toSpecifiedChar().apply {
                this.charWidth = charWidth
                this.indexInChapter = dealSize + charIndex
                this.markStyles = marks?.match(charIndex)
            }

            val charPaddingStart = charPaddingStart(charIndex, txtChar, src)
            val charPaddingEnd = charPaddingEnd(charIndex, txtChar, src)
            val charPadding = charPaddingStart + charPaddingEnd
            txtChar.let {
                it.paddingStart = charPaddingStart
                it.paddingEnd = charPaddingEnd
            }

            if (charWidth + width + charPaddingStart + charPaddingEnd <= usableWidth) {
                txtLine.chars.add(txtChar)
                width += (charWidth + charPadding)
                charIndex++
                continue
            } else if (charWidth + width + charPaddingStart <= usableWidth) {
                // 还能再加最后一个字符
                // 可能需要加的是链接符
                val linkChar = maybeLinkChar(charIndex, txtChar, src)
                if (linkChar != null) {
                    linkChar.charWidth = paint.measureText(linkChar.char.toString())
                    txtLine.chars.add(linkChar)
                    txtLine.surpassLength =
                        usableWidth - (linkChar.charWidth + width + linkChar.paddingStart)//剩下这些长度是空的
                } else {
                    txtChar.paddingEnd = 0f
                    txtChar.isCombinesEnd = false
                    txtLine.chars.add(txtChar)
                    txtLine.surpassLength =
                        usableWidth - (charWidth + width + charPaddingStart)//剩下这些长度是空的
                }
                break
            } else {
                // 最后一个字符可能需要加的是链接符
                val linkChar = maybeLinkChar(charIndex - 1, txtLine.last(), src)

                if (linkChar != null && txtLine.size >= 2) {
                    txtLine.lastOrNull()?.let {
                        width -= (it.charWidth + it.paddingStart + it.paddingEnd)
                    }
                    // 倒数第二个
                    val lastButOne = txtLine.chars[txtLine.size - 2]
                    // 倒数第二个是英文
                    if (lastButOne.charType() == CharType.ENGLISH) {
                        linkChar.charWidth = paint.measureText(linkChar.char.toString())
                        txtLine.chars[txtLine.lastIndex()] = linkChar // 替换最后一个字符
                        //剩下这些长度是空的
                        txtLine.surpassLength =
                            usableWidth - (linkChar.charWidth + width + linkChar.paddingStart)
                    } else {
                        txtLine.chars.removeAt(txtLine.size - 1)
                        lastButOne.paddingEnd = 0f
                        txtLine.surpassLength = usableWidth - lastButOne.paddingEnd
                    }
                } else {
                    //剩下这些长度是空的
                    txtLine.surpassLength = usableWidth - width +
                            (txtLine.lastOrNull()?.paddingEnd ?: 0f)//最后一个字符的Padding也算是空隙
                    txtLine.last().let {
                        it.paddingEnd = 0f
                        it.isCombinesEnd = false
                    }
                }
                break
            }
        }
        adjustPadding(txtLine)
        calculateX(txtLine, padding[0].toFloat() + paint.measureText(txtLine.lineIndentationSymbol))
        return txtLine
    }

    /**
     * @param src 源字符串
     * @param charIndex 开始位置
     * 如果[src]从[charIndex]位置开始 是以[lineSymbol]中的其中一项开头的，返回这一项，否则返回null
     * @return 非空代表[src]从[charIndex]位置开始是换行符，并返回该换行符，null代表不是以换行符
     */
    private fun newlineSymbol(charIndex: Int, src: String): String? {
        val subSrc = src.substring(charIndex)
        for (s in lineSymbol) {
            if (subSrc.startsWith(s)) {
                return s
            }
        }
        return null
    }

    private fun charPaddingStart(charIndex: Int, txtChar: TxtChar, src: String): Float {
        if (charIndex == 0) {
            return 0f
        }
        val charType = txtChar.charType()
        return if (charType == CharType.CHINESE || charType != src[charIndex - 1].charType()) {
            txtChar.isCombinesStart = true
            txtChar.paddingStart
        } else {
            0f
        }
    }

    private fun charPaddingEnd(charIndex: Int, txtChar: TxtChar, src: String): Float {
        if (charIndex == src.length - 1) {
            return 0f
        }
        val charType = txtChar.charType()
        return if (charType == CharType.CHINESE || charType != src[charIndex + 1].charType()) {
            txtChar.isCombinesEnd = true
            txtChar.paddingEnd
        } else {
            0f
        }
    }

    /**
     * 重新调正字符的padding
     */
    private fun adjustPadding(txtLine: TxtLine) {
        val combinesEndChars = txtLine.chars.filter { it.isCombinesEnd && it !is LinkChar }
        val combinesStartChars = txtLine.chars.filter { it.isCombinesStart && it !is LinkChar }
        if (combinesEndChars.isEmpty() && combinesStartChars.isEmpty()) {
            return
        }
        val averageSurpassLength =
            txtLine.surpassLength / (combinesEndChars.size + combinesStartChars.size)
        combinesStartChars.forEach {
            it.paddingStart += averageSurpassLength
        }
        combinesEndChars.forEach {
            it.paddingEnd += averageSurpassLength
        }
    }

    /**
     * 计算字符的x
     */
    private fun calculateX(txtLine: TxtLine, paddingStart: Float) {
        var x = paddingStart
        txtLine.chars.forEach {
            x += it.paddingStart
            it.x = x
            x += (it.charWidth + it.paddingEnd)
        }
    }

    /**
     * 指定位置[charIndex]字符串可能需要链接符
     * @return LinkChar ,不需要链接符返回null
     */
    private fun maybeLinkChar(charIndex: Int, char: TxtChar, src: String): LinkChar? {
        if (char.charType() != CharType.ENGLISH) {
            return null
        }
        if (charIndex >= src.length - 1) {
            return null
        }
        if (src[charIndex + 1].charType() == CharType.ENGLISH) {
            return LinkChar()
        }
        return null
    }
}