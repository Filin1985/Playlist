package com.example.playlistmaker.ui.player.customView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.model.PlayButtonState

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val playImageBitmap: Bitmap?
    private val pauseImageBitmap: Bitmap?
    var playButtonState: PlayButtonState = PlayButtonState.STATE_PLAY
        set(value) {
            field = value
            invalidate()
        }
    private var imageRect = RectF(0f, 0f, 0f, 0f)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playImageBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_playImageResId)?.toBitmap()
                pauseImageBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_pauseImageResId)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        when (playButtonState) {
            PlayButtonState.STATE_PLAY -> playImageBitmap?.let {
                canvas.drawBitmap(playImageBitmap, null, imageRect, null)
            }

            PlayButtonState.STATE_PAUSE -> pauseImageBitmap?.let {
                canvas.drawBitmap(pauseImageBitmap, null, imageRect, null)
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                playButtonState = when (playButtonState) {
                    PlayButtonState.STATE_PLAY -> PlayButtonState.STATE_PAUSE
                    PlayButtonState.STATE_PAUSE -> PlayButtonState.STATE_PLAY
                }
                invalidate()
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}