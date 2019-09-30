package com.glong.txtlib.entry

import com.glong.txtlib.entry.chars.LinkChar
import com.glong.txtlib.entry.chars.TxtChar

/**
 * 行数据
 * @author guolong
 * @since 2019/9/27
 */
class TxtLine {
    /**
     * 该行所有字符
     */
    val chars = mutableListOf<TxtChar>()

    /**
     * 是否是段首
     */
    var isParagraphStart = false

    /**
     * 是否是段尾（段落的最后一行是false）
     */
    var isParagraphEnd = false

    /**
     * 在当前章节中的行索引
     */
    var indexInChapter: Int = 0

    /**
     * 整行画完后，还有这些长度像素是空的
     * [isParagraphEnd]是false时，为了draw的时候前后对齐，可以将这些多余空隙平均分配给整行的所有字符
     */
    internal var surpassLength: Float = 0f

    /**
     * chars的字符长度
     */
    val size: Int
        get() = chars.size

    /**
     * chars 除LinkChar的size
     */
    val validSize: Int
        get() = chars.filter { it !is LinkChar }.size

    val lineString: String
        get() = chars.toString()

    /**
     * 如果[isParagraphEnd]是true，即最后一行时，该值不为空
     */
    var lineSymbol: String = ""

    /**
     * 缩进符 如果[isParagraphStart]是true，即第一行时，该值不为空
     */
    var lineIndentationSymbol = ""

    /**
     * line 的top位置
     */
    var y: Float = 0f

    /**
     * 上下间距
     */
    var lineTopSpace: Float = 0f
    var lineBottomSpace: Float = 0f

}