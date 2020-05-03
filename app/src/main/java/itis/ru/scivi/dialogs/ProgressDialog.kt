package itis.ru.scivi.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import itis.ru.scivi.R

class ProgressDialog : DialogFragment() {

    companion object {
        private var progressDialog: ProgressDialog? = null

        fun newInstance(): ProgressDialog {
            return if (progressDialog == null) {
                progressDialog = ProgressDialog()
                progressDialog!!
            } else progressDialog!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_progress, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        //dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return view
    }

}