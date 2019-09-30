package com.glong.txtlib.utils

import android.graphics.Paint
import com.glong.txtlib.entry.Mark
import com.glong.txtlib.entry.TxtPage

/**
 * @author guolong
 * @since 2019/9/30
 */
object TxtPageUtil {
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
        measureHeight: Float, lineSpace: Float,
        padding: IntArray,//View的padding
        paint: Paint, marks: List<Mark>? = null
    ): MutableList<TxtPage>? {
        val lines = TxtBreakUtil.breakTxt(src, measureWidth, padding, paint, marks)
        if (lines.isNullOrEmpty()) return null

        val result = mutableListOf<TxtPage>()

        val oriFontMetrics = paint.fontMetrics
        // 文字高度
        val textHeight = oriFontMetrics.let { it.bottom - it.top }
        val usableHeight = measureHeight - padding[1] + padding[3]//可用的高度
        /*
         * 假设一页最多可以显示x行，那么，
         * (textHeight * x) + lineSpace * (x - 1) == usableHeight -->
         * x = (usableHeight + lineSpace)/(textHeight + lineSpace)
         */
        val maxLine: Int = ((usableHeight + lineSpace) / (textHeight + lineSpace)).toInt() //最多显示行数
        if (maxLine <= 1) {
            return null
        }
        val realitySpace = (usableHeight - maxLine * textHeight) / (maxLine - 1)
        var page: TxtPage? = null
        var y = padding[1].toFloat()
        for (index in lines.indices) {
            val line = lines[index]
            when (index % maxLine) {
                0 -> {
                    page = TxtPage().apply { this.pageIndex = result.size }
                    y = padding[1].toFloat()
                    page.lines.add(line.apply {
                        this.lineTopSpace = 0f
                        this.lineBottomSpace = realitySpace / 2
                        this.y = y
                    })
                    y += (realitySpace / 2 + textHeight)
                }
                maxLine - 1 -> {
                    page?.lines?.add(line.apply {
                        this.lineTopSpace = realitySpace / 2
                        this.lineBottomSpace = 0f
                        this.y = y
                    })
                    y += (realitySpace / 2 + textHeight)
                    if (page != null) {
                        result.add(page)
                    }
                }
                else -> {
                    page?.lines?.add(line.apply {
                        this.lineTopSpace = realitySpace / 2
                        this.lineBottomSpace = realitySpace / 2
                        this.y = y
                    })
                    y += (realitySpace + textHeight)
                }
            }
        }
        return result
    }
}