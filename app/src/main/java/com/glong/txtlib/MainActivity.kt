package com.glong.txtlib

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glong.txtlib.view.MarkStyle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtView.addMarkStyles(MarkStyle(0, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = Color.RED
        }))
        txtView.addMark(0, 50, 0)
            .addMark(100, 120, 0)
            .commit()
    }
}
