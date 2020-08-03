package com.confirmit.testsdkapp.domain

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.*
import android.view.View.OnTouchListener
import android.widget.ImageView
import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.viewmodels.models.ProgramModel

class OverlayService : Service(), OnTouchListener {
    companion object {
        val LAYOUT_FLAG: Int
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }

        var isRunning: Boolean = false

    }

    private var lastAction = 0
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private lateinit var params: WindowManager.LayoutParams

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        //Inflate the chat head layout we created
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_icon, null)
        //Add the view to the window.
        params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
        //Specify the chat head position
        params.gravity = Gravity.TOP or Gravity.LEFT //Initially view will be added to top-left corner
        params.x = 0
        params.y = 100
        //Add the view to the window
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager!!.addView(overlayView, params)

        //Drag and move chat head using user's touch action.
        val chatHeadImage: ImageView = overlayView!!.findViewById<View>(R.id.chat_head_profile_iv) as ImageView
        chatHeadImage.setOnTouchListener(this)

        isRunning = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayView != null) windowManager!!.removeView(overlayView)

        isRunning = false
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                //remember the initial position.
                initialX = params.x
                initialY = params.y
                //get the touch location
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                lastAction = event.action

                return true
            }
            MotionEvent.ACTION_UP -> {
                //As we implemented on touch listener with ACTION_MOVE,
                //we have to check if the previous action was ACTION_DOWN
                //to identify if the user clicked the view or not.
                if (lastAction == MotionEvent.ACTION_DOWN) { //Open the chat conversation click.
                    OverlaySurveyManager.getProgram()?.let {
                        val model = ProgramModel(it)
                        TriggerSDK.notifyEvent(it.serverId, it.programKey, "onTestEvent", model.getCustomData())
                    }
                } else {
                    // move and dragged
                    val displayMetrics = DisplayMetrics()
                    windowManager!!.defaultDisplay.getMetrics(displayMetrics)
                    val height = displayMetrics.heightPixels + getNavigationBarHeight()
                    val iconHeight = v!!.height
                    val threshHold = height - iconHeight * 2
                    if (event.rawY > threshHold) {
                        stopSelf()
                    }
                }
                lastAction = event.action
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                //Calculate the X and Y coordinates of the view.
                params.x = initialX + (event.rawX - initialTouchX).toInt()
                params.y = initialY + (event.rawY - initialTouchY).toInt()
                //Update the layout with new X & Y coordinate
                windowManager!!.updateViewLayout(overlayView, params)
                lastAction = event.action
                return true
            }
        }
        return false
    }

    private fun getNavigationBarHeight(): Int {
        val metrics = DisplayMetrics()
        windowManager!!.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        windowManager!!.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight) realHeight - usableHeight else 0
    }
}