package de.boscall

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_unit_reader.*
import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.core.ViewFinderView
import me.dm7.barcodescanner.zxing.ZXingScannerView

class UnitReaderActivity : Activity(), ZXingScannerView.ResultHandler {
    private lateinit var mScannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_reader)
        //mScannerView = ZXingScannerView(this)
        mScannerView = object : ZXingScannerView(this) {
            override fun createViewFinderView(context: Context): IViewFinder {
                return ResizableViewFinder(context)
            }
        }
        mScannerView.setAspectTolerance(0.6f)
        content_frame.addView(mScannerView)
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(result: Result) {
        Toast.makeText(this, getString(R.string.unitReader_toast_unit_found), Toast.LENGTH_SHORT).show()
        val resultIntent = Intent()
        resultIntent.putExtra("unit", result.text)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    inner class ResizableViewFinder(context: Context) : ViewFinderView(context) {
        override fun onDraw(canvas: Canvas?) {
            drawViewFinderBorder(canvas)
            drawViewFinderMask(canvas)
            drawLaser(canvas)
            drawViewFinderBorder(canvas)
        }

        override fun getFramingRect(): Rect {
            val rect = Rect()
            content_frame.getDrawingRect(rect)
            return rect
        }
    }
}
