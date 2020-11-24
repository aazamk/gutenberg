package com.example.mybook.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewAnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.example.mybook.R
import com.example.mybook.databinding.ViewSearchBinding

class FlSearchView : CardView {
    private var animateSearchView = false
    private var searchMenuPosition = 0
    private var searchHint: String? = null
    private var searchTextColor = 0
    private var searchIconColor: Int? = null
    private var mOldQueryText: CharSequence? = null
    private var mUserQuery: CharSequence? = null
    private val hasAdapter = false
    private var hideSearch = false
    lateinit var b: ViewSearchBinding
    private var listenerQuery: OnQueryTextListener? = null
    private var visibilityListener: OnVisibilityListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MaterialSearchView, 0, 0)
        val inflater = LayoutInflater.from(context)
        b = DataBindingUtil.inflate(inflater, R.layout.view_search, this, true)
        animateSearchView = a.getBoolean(R.styleable.MaterialSearchView_search_animate, true)
        searchMenuPosition = a.getInteger(R.styleable.MaterialSearchView_search_menu_position, 0)
        searchHint = a.getString(R.styleable.MaterialSearchView_search_hint)
        searchTextColor = a.getColor(
            R.styleable.MaterialSearchView_search_text_color, resources.getColor(
                R.color.black
            )
        )
        searchIconColor = a.getColor(
            R.styleable.MaterialSearchView_search_icon_color, resources.getColor(
                R.color.black
            )
        )
        b.imgClear.setOnClickListener(mOnClickListener)
        b.editText.addTextChangedListener(mTextWatcher)
        b.editText.setOnEditorActionListener(mOnEditorActionListener)
        var imeOptions = a.getInt(R.styleable.MaterialSearchView_search_ime_options, -1)
        if (imeOptions != -1) {
            imeOptions = imeOptions
        }
        var inputType = a.getInt(R.styleable.MaterialSearchView_search_input_type, -1)
        if (inputType != -1) {
            inputType = inputType
        }
        val focusable: Boolean
        focusable = a.getBoolean(R.styleable.MaterialSearchView_search_focusable, true)
        b.editText.setFocusable(focusable)
        a.recycle()
        b.editText.setHint(getSearchHint())
        b.editText.setTextColor(textColor)
    }

    private val mOnClickListener =
        OnClickListener { v ->
            if (v === b.imgClear) {
                setSearchText(null)
                b.editText.clearFocus()
                if (listenerQuery != null) {
                    listenerQuery!!.onSearchBackClick()
                }
            }
        }

    /**
     * Callback to watch the text field for empty/non-empty
     */
    private val mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {
            if (s.length > 0) {
                submitText(s)
                b.relHolder.setBackgroundResource(R.drawable.bg_focused)
            } else if (before > 0 && s.length == 0) {
                submitText("")
                b.relHolder.setBackgroundResource(R.drawable.bg_unfocused)
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val mOnEditorActionListener =
        OnEditorActionListener { v, actionId, event ->
            onSubmitQuery()
            true
        }

    private fun submitText(s: CharSequence) {
        mUserQuery = b.editText.getText()
        updateClearButton()
        if (listenerQuery != null && !TextUtils.equals(s, mOldQueryText)) {
            listenerQuery!!.onQueryTextChange(s.toString())
        }
        mOldQueryText = s.toString()
    }

    fun onSubmitQuery() {
        if (listenerQuery != null) {
            listenerQuery!!.onQueryTextSubmit(b.editText.getText().toString())
        }
    }

    private fun updateClearButton() {
        val hasText = !TextUtils.isEmpty(b.editText.getText())
        b.imgClear.setVisibility(if (hasText) VISIBLE else GONE)
    }

    fun setQuery(query: CharSequence?, submit: Boolean) {
        b.editText.setText(query)
        if (query != null) {
            mUserQuery = query
        }
        // If the query is not empty and submit is requested, submit the query
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery()
        }
    }
    /**
     * Returns the IME options set on the query text field.
     */
    /**
     * Sets the IME options on the query text field.
     */
    var imeOptions: Int
        get() = b.editText.getImeOptions()
        set(imeOptions) {
            b.editText.setImeOptions(imeOptions)
        }
    /**
     * Returns the input type set on the query text field.
     */
    /**
     * Sets the input type on the query text field.
     */
    var inputType: Int
        get() = b.editText.getInputType()
        set(inputType) {
            b.editText.setInputType(inputType)
        }
    val isVisible: Boolean
        get() = visibility == VISIBLE

    fun setSearchText(queryText: String?) {
        b.editText.setText(queryText)
    }

    fun hideSearch() {

        setSearchText("")
        if (animateSearchView) {
            if (Build.VERSION.SDK_INT >= 21) {
                val animatorHide = ViewAnimationUtils.createCircularReveal(
                    this,  // View
                    centerX,  // center x
                    convertDpToPixel(23f).toInt(),  // center y
                    Math.hypot(width.toDouble(), height.toDouble()).toFloat(), 0f
                )
                animatorHide.setStartDelay((if (hasAdapter) ANIMATION_DURATION else 0.toLong()) as Long)
                animatorHide.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        if (visibilityListener != null) {
                            visibilityListener!!.onClose()
                        }
                        visibility = GONE
                    }
                })
                animatorHide.start()
            } else {
                visibility = GONE
            }
        }
    }



    // search searchHint
    fun getSearchHint(): String? {
        return if (TextUtils.isEmpty(searchHint)) {
            "Search"
        } else searchHint
    }

    fun setSearchHint(searchHint: String?) {
        this.searchHint = searchHint
        invalidate()
        requestFocus()
    }

    fun removeFocus() {
        val view: View = editText
        if (view is EditText) {
            Log.d(LOG_TAG, "removeEditTextFocus: ")
            view.clearFocus()
        }
    }

    fun setSearchIconColor(searchIconColor: Int) {
        this.searchIconColor = searchIconColor
        invalidate()
        requestFocus()
    }

    // text color
    var textColor: Int
        get() = searchTextColor
        set(textColor) {
            searchTextColor = textColor
            invalidate()
            requestFocus()
        }

    /**
     * Get views
     */
    val editText: EditText
        get() = b.editText
    val imageBack: ImageView
        get() = b.imgBack
    val imageClear: ImageView
        get() = b.imgClear

    fun showSearch() {
        hideSearch = false
        visibility = VISIBLE
        if (animateSearchView) if (Build.VERSION.SDK_INT >= 21) {
            val animatorShow = ViewAnimationUtils.createCircularReveal(
                this,  // view
                centerX,  // center x
                convertDpToPixel(23f).toInt(), 0f,  // start radius
                Math.hypot(width.toDouble(), height.toDouble()).toFloat() // end radius
            )
            animatorShow.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (visibilityListener != null) {
                        visibilityListener!!.onOpen()
                    }
                }
            })
            animatorShow.start()
        }
    }

    /**
     * Interface
     */
    fun addQueryTextListener(listenerQuery: OnQueryTextListener?) {
        this.listenerQuery = listenerQuery
    }

    interface OnQueryTextListener {
        fun onQueryTextSubmit(query: String?): Boolean
        fun onSearchBackClick()
        fun onQueryTextChange(newText: String?): Boolean
    }

    fun setOnVisibilityListener(visibilityListener: OnVisibilityListener?) {
        this.visibilityListener = visibilityListener
    }

    interface OnVisibilityListener {
        fun onOpen(): Boolean
        fun onClose(): Boolean
    }

    /**
     * Helpers
     */
    fun setDrawableTint(resDrawable: Drawable, resColor: Int) {
        resDrawable.colorFilter = PorterDuffColorFilter(resColor, PorterDuff.Mode.SRC_ATOP)
        resDrawable.mutate()
    }

    fun convertDpToPixel(dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi / 160f)
    }
    private val centerX: Int
        private get() {
            val icons = (width - convertDpToPixel(21 * (1 + searchMenuPosition).toFloat())).toInt()
            val padding = convertDpToPixel(searchMenuPosition * 21.toFloat()).toInt()
            return icons - padding
        }

    companion object {
        const val LOG_TAG = "FlSearchView"
        private const val ANIMATION_DURATION = 250
    }
}