package center.techostartup.raksadriver.view.loading

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import center.techostartup.raksadriver.R
import center.techostartup.raksadriver.data.local.AppSharedPreference
import center.techostartup.raksadriver.data.remote.repository.TokenRepository
import center.techostartup.raksadriver.data.remote.request.RefreshTokenRequest
import center.techostartup.raksadriver.data.remote.response.NewTokenResponse
import center.techostartup.raksadriver.utils.AppConstants
import center.techostartup.raksadriver.utils.connectivity.base.ConnectivityProvider
import center.techostartup.raksadriver.view.base.BaseActivity
import center.techostartup.raksadriver.view.main.MainActivity
import center.techostartup.raksadriver.view.phonenumberlogin.PhoneNumberLoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadingActivity : BaseActivity() {

    private lateinit var tvInternetConnection: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        tvInternetConnection = findViewById(R.id.tv_internet_connection)

        installSplashScreen()
    }

    override fun networkStateAction(state: ConnectivityProvider.NetworkState) {
        if (state.hasInternet()) {
            refreshToken()
        } else {
            showNoInternetConnectionDialog()
        }
    }

    private fun refreshToken() {
        val sharedPreference = AppSharedPreference(application)
        val refreshToken = sharedPreference.get(AppConstants.REFRESH_TOKEN)
        val call = TokenRepository.refreshToken(application, RefreshTokenRequest(refreshToken))

        call.enqueue(object : Callback<NewTokenResponse> {
            override fun onResponse(
                call: Call<NewTokenResponse>,
                response: Response<NewTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val newToken = response.body()

                    newToken?.refresh?.let {
                        sharedPreference.insert(
                            AppConstants.REFRESH_TOKEN,
                            it
                        )
                    }
                    newToken?.access?.let { sharedPreference.insert(AppConstants.ACCESS_TOKEN, it) }

                    // get driver data after refresh token
                    val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // when logging in is failed, navigate to login screen
                    val intent = Intent(this@LoadingActivity, PhoneNumberLoginActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }

            override fun onFailure(call: Call<NewTokenResponse>, t: Throwable) {
                Log.d("TAG.....", "onFailure: ${t.toString()}")
            }
        })
    }
}