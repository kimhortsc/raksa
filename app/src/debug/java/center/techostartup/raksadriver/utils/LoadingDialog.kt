package center.techostartup.raksadriver.utils

import center.techostartup.raksadriver.R
import android.app.Activity
import androidx.appcompat.app.AlertDialog


class LoadingDialog(private val activity: Activity?) {

    private var alertDialog: AlertDialog? = null

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading, null))
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog?.show()
    }

    fun dismissDialog() {
        alertDialog?.dismiss()
    }
}