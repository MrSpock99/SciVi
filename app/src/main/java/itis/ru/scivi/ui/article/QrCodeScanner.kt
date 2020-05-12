package itis.ru.scivi.ui.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import itis.ru.scivi.utils.Const
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrCodeScanner : AppCompatActivity(), ZXingScannerView.ResultHandler {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, QrCodeScanner::class.java)
        }
    }

    private var mScannerView: ZXingScannerView? = null
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        // Programmatically initialize the scanner view
        mScannerView = ZXingScannerView(this)
        // Set the scanner view as the content view
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        // Register ourselves as a handler for scan results.
        mScannerView!!.setResultHandler(this)
        // Start camera on resume
        mScannerView!!.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        // Stop camera on pause
        mScannerView!!.stopCamera()
    }

    override fun handleResult(res: Result?) {
        val intent = Intent()
        intent.putExtra(Const.Args.KEY_QR_CODE, res!!.getText())
        setResult(RESULT_OK, intent)
        finish()
    }
}