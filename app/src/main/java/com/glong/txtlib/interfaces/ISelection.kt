package com.glong.txtlib.interfaces

/**
 * @author guolong
 * @since 2019/9/29
 */
interface ISelection {

    /**
     * 是否已选中
     */
    var isSelected: Boolean

    /**
     * 已被标记的类型
     * 泛型Int 是[com.glong.txtdemo.view.TxtView.markStyles]的索引
     */
    var markStyles: MutableSet<Int>?
}