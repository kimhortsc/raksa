package center.techostartup.raksadriver.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import center.techostartup.raksadriver.utils.connectivity.base.ConnectivityProvider

open class BaseActivity : AppCompatActivity(), ConnectivityProvider.ConnectivityStateListener,
    NoConnectionDialogFragment.NoticeDialogListener {

    private lateinit var noConnectionDialogFragment: NoConnectionDialogFragment
    private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noConnectionDialogFragment = NoConnectionDialogFragment()

        networkStateAction(provider.getNetworkState())
    }

    override fun onStart() {
        super.onStart()
        provider.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        if(!state.hasInternet()){
            showNoInternetConnectionDialog()
        }
    }

    override fun onDialogPositiveClick() {
        networkStateAction(provider.getNetworkState())
    }

    fun showNoInternetConnectionDialog() {
        val fm = supportFragmentManager
        val oldFragment = fm.findFragmentByTag("NoConnectionDialogFragment")

        if(oldFragment != null && oldFragment.isAdded){
            fm.beginTransaction()
                .remove(oldFragment).
                commitNow()
        }

        noConnectionDialogFragment.show(supportFragmentManager, "NoConnectionDialogFragment")
    }

    open fun networkStateAction(state: ConnectivityProvider.NetworkState) {
        if(!state.hasInternet()){
            showNoInternetConnectionDialog()
        }
    }

    fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }
}