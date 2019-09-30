package com.glong.txtlib.interfaces

/**
 * @author guolong
 * @since 2019/9/29
 */
interface IDraw {
    // 主要用于draw的时候确定字符前后间距
    var paddingStart: Float
    var paddingEnd: Float

    // 文字的绘制的开始位置（这个位置是真实位置，与padding无关）
    var x:Float

    // 字符宽度
    var charWidth: Float
}