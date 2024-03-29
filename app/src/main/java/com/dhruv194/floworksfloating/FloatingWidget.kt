package com.dhruv194.floworksfloating

import android.app.Service
import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.getIntentOld
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible


class FloatingWidget : Service() {

    private lateinit var floatView : ViewGroup
    private lateinit var floatWindowLayoutParams : WindowManager.LayoutParams
    private lateinit var floatWindowLayoutParams1 : WindowManager.LayoutParams
    private var  LAYOUT_TYPE : Int? = null
    private lateinit var windowManager: WindowManager
    private lateinit var btnlaunchapp: Button
    private lateinit var btnicon : ImageButton
    private lateinit var textviewheading : TextView
    private lateinit var checkFrom : String

    override fun onBind(p0: Intent?): IBinder? {
        checkFrom = p0!!.getStringExtra("value").toString()
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkFrom = intent!!.getStringExtra("value").toString()
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onCreate() {
        super.onCreate()

        val metrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        Toast.makeText(this, "Created Flaoting$width",Toast.LENGTH_SHORT).show()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val infalter = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        floatView = infalter.inflate(R.layout.floating_widget_layout,null) as ViewGroup

        btnlaunchapp = floatView.findViewById(R.id.launch_app)
        btnicon = floatView.findViewById(R.id.icon_btn)
        textviewheading = floatView.findViewById(R.id.heading_textView)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        else{
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST
        }


            floatWindowLayoutParams= WindowManager.LayoutParams(
                (width *0.15f).toInt(),
                (height *0.10f).toInt(),
                LAYOUT_TYPE!!,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )


       floatWindowLayoutParams.gravity = Gravity.RIGHT
        floatWindowLayoutParams.x = 0
        floatWindowLayoutParams.y=0
       // floatWindowLayoutParams.height = (height*0.3f).toInt()
        /*Handler(Looper.getMainLooper()).postDelayed({
            floatWindowLayoutParams.height = (height*0.3f).toInt()
        }, 1000)*/

        windowManager.addView(floatView,floatWindowLayoutParams)





        btnicon.setOnClickListener{
            floatWindowLayoutParams1= WindowManager.LayoutParams(
                (width *0.30f).toInt(),
                (height *0.30f).toInt(),
                LAYOUT_TYPE!!,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            floatWindowLayoutParams1.gravity = Gravity.RIGHT
            floatWindowLayoutParams1.x = 0
            floatWindowLayoutParams1.y=0
            btnlaunchapp.isVisible = true
            textviewheading.isVisible = true
            btnicon.isVisible = false
            windowManager.updateViewLayout(floatView,floatWindowLayoutParams1)

        }
       /*val intent = getIntentOld("www.floworksfloating.com")
        val action = intent.action
        val data: Uri? = intent.data
        if(data!=null){
            Toast.makeText(this,"Here",Toast.LENGTH_SHORT).show()
            floatWindowLayoutParams1= WindowManager.LayoutParams(
                (width *0.30f).toInt(),
                (height *0.30f).toInt(),
                LAYOUT_TYPE!!,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            floatWindowLayoutParams1.gravity = Gravity.RIGHT
            floatWindowLayoutParams1.x = 0
            floatWindowLayoutParams1.y=0
            btnlaunchapp.isVisible = true
            textviewheading.isVisible = true
            btnicon.isVisible = false
            windowManager.updateViewLayout(floatView,floatWindowLayoutParams1)
        }*/
        btnlaunchapp.setOnClickListener {
            stopSelf()
            windowManager.removeView(floatView)

            val backintent = Intent(this@FloatingWidget, MainActivity::class.java)
            backintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(backintent)

        }



        floatView.setOnTouchListener(object : View.OnTouchListener{
            val updatedFloatWindowLayoutParam = floatWindowLayoutParams
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0

            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when(p1!!.action){
                    MotionEvent.ACTION_DOWN->{
                    x = updatedFloatWindowLayoutParam.x.toDouble()
                    y = updatedFloatWindowLayoutParam.y.toDouble()

                    px = p1.rawX.toDouble()
                    py = p1.rawY.toDouble()
                    }
                    MotionEvent.ACTION_MOVE->{
                        updatedFloatWindowLayoutParam.x = (x+p1.rawX -px).toInt()
                        updatedFloatWindowLayoutParam.y = (y+p1.rawY -py).toInt()
                        updatedFloatWindowLayoutParam.width = (width*0.30f).toInt()
                        updatedFloatWindowLayoutParam.height = (height*0.30f).toInt()

                        windowManager.updateViewLayout(floatView, updatedFloatWindowLayoutParam)
                    }
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        windowManager.removeView(floatView)
    }
}