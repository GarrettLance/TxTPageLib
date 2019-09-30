package com.glong.txtlib.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.glong.txtdemo.Test.contentMixture
import com.glong.txtlib.entry.Mark
import com.glong.txtlib.entry.TxtLine
import com.glong.txtlib.entry.TxtPage
import com.glong.txtlib.entry.chars.TxtChar
import com.glong.txtlib.utils.TxtPageUtil
import kotlin.math.abs

/**
 * @author guolong
 * @since 2019/9/27
 */
class TxtView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    private var content = contentMixture

    private var pages: MutableList<TxtPage>? = null
    private val lineSpace = 10f

    internal val markStyles: MutableList<MarkStyle> = mutableListOf(MarkStyle(0, Paint()))
    private val marks = mutableListOf<Mark>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        initPaint()
    }

    private fun initPaint() {
        paint.textSize = 55f
        paint.color = Color.BLACK
    }

    fun setText(text: String) {
        content = text
        if (width != 0) {
            breakContent()
            invalidate()
        } else {
            requestLayout()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (content.isNotEmpty() && pages == null) {
            breakContent()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (pages.isNullOrEmpty()) return
        val page = pages!![0]
        val fm = paint.fontMetrics
        for (txtLine in page.lines) {
            for (txtChar in txtLine.chars) {
                // top距离baseline的距离
                val baseline = txtLine.y + abs(fm.top)
                canvas.drawText(txtChar.char.toString(), txtChar.x, baseline, paint)
            }
        }
    }

    /**
     * 画选中部分
     */
    open fun onDrawSelected(canvas: Canvas, lines: List<TxtLine>) {
        lines.forEach { txtLine ->
            txtLine.chars.forEach {

            }
        }
    }

    /**
     * 长按选中操作 手指按下时回调
     * @param txtChar 手指按下时的位置对应的 字符
     */
    open fun onSelectedWhenTouchDown(txtChar: TxtChar) {

    }

    /**
     * 长按选中操作 手指移动时回调
     * @param lines 选中的行集合
     */
    open fun onSelectedWhenTouchMove(lines: List<TxtLine>) {

    }

    /**
     * 长按选中操作 当手指松开时回调
     * @param lines 选中的行集合
     */
    open fun onSelectedWhenTouchUp(lines: List<TxtLine>) {

    }

    /**
     * 增加标记类型
     */
    fun addMarkStyles(markStyle: MarkStyle) {
        markStyles.add(markStyle)
    }

    /**
     * 标记当前选中的字符集合
     * @param markStyle 来自[markStyles]
     */
    fun markSelected(markStyle: MarkStyle) {
        if (markStyle !in markStyles) {
            throw IllegalArgumentException("markStyle 不在 markStyles集合内")
        }
    }

    fun markSelected(styleIndex: Int) {
        val style = markStyles[styleIndex]

    }

    /**
     *
     */
    fun addMark(charStartIndexInChapter: Int, charEndIndexInChapter: Int, styleIndex: Int): TxtView {
        if (styleIndex >= markStyles.size) {
            throw IllegalArgumentException("markStyle 不在 markStyles集合内")
        }
        marks.add(Mark(charStartIndexInChapter, charEndIndexInChapter, styleIndex))
        return this
    }

    fun commit() {
        if (width != 0 && height != 0) {
            invalidateContent()
        }
    }

    /**
     * 更新内容
     */
    fun invalidateContent() {
        breakContent()
        invalidate()
    }

    fun onDrawMark() {

    }

    private fun breakContent() {
        val padding = intArrayOf(paddingStart, paddingTop, paddingEnd, paddingBottom)
        pages = TxtPageUtil.breakTxt(content, measuredWidth.toFloat(), measuredHeight.toFloat(), lineSpace, padding, paint)
    }
}
