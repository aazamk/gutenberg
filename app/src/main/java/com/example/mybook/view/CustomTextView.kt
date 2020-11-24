package com.example.mybook.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.SparseArray
import androidx.appcompat.widget.AppCompatTextView
import com.example.mybook.R


class CustomFontTextView : AppCompatTextView {
    private val mont_semibold = 0
    private val mont_regular = 1


    constructor(context: Context?) : super(context!!) {}


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        parseAttributes(context, attrs)
    }

    private fun parseAttributes(context: Context, attrs: AttributeSet?) {
        val values = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView)
        val typefaceValue = values.getInt(R.styleable.CustomFontTextView_custometype_face, 0)
        values.recycle()
        setTypeface(obtaintTypeface(context, typefaceValue))
    }


    @Throws(IllegalArgumentException::class)
    private fun obtaintTypeface(context: Context, typefaceValue: Int): Typeface? {
        var typeface = mTypefaces[typefaceValue]
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue)
            mTypefaces.put(typefaceValue, typeface)
        }
        return typeface
    }


    private fun createTypeface(context: Context, typefaceValue: Int): Typeface {
        val typeface: Typeface
        typeface =
            when (typefaceValue) {
                mont_regular -> Typeface.createFromAsset(
                    context.assets,
                    "fonts/Montserrat-Regular.ttf"
                )
                mont_semibold -> Typeface.createFromAsset(
                    context.assets,
                    "fonts/Montserrat-SemiBold.ttf"
                )

                else -> throw IllegalArgumentException("Unknown `typeface` attribute value $typefaceValue")
            }
        return typeface
    }

    override fun performClick(): Boolean {
        // do what you want
        super.performClick()
        return true
    }

    companion object {

        private val mTypefaces = SparseArray<Typeface?>(7)
    }
}