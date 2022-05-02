package center.techostartup.raksadriver.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import center.techostartup.raksadriver.R
import center.techostartup.raksadriver.data.local.AppSharedPreference
import center.techostartup.raksadriver.data.model.Driver
import center.techostartup.raksadriver.data.remote.repository.TokenRepository
import center.techostartup.raksadriver.data.remote.request.RefreshTokenRequest
import center.techostartup.raksadriver.data.remote.response.NewTokenResponse
import center.techostartup.raksadriver.utils.AppConstants
import center.techostartup.raksadriver.view.driverprofile.DriverProfileFragment
import center.techostartup.raksadriver.view.driverprofile.DriverViewModel
import center.techostartup.raksadriver.view.home.HomeFragment
import center.techostartup.raksadriver.view.phonenumberlogin.PhoneNumberLoginActivity
import center.techostartup.raksadriver.view.summary.SummaryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var selectedFragment: Fragment



    private var homeFragment: HomeFragment? = null
    private var summaryFragment: SummaryFragment? = null
    private var driverProfileFragment: DriverProfileFragment? = null

    private lateinit var bnvMain: BottomNavigationView
    private lateinit var progressBar: ProgressBar

    private lateinit var driver: Driver
    private var bundle = Bundle()

   // late init var driverViewModel: DriverViewModel

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        installSplashScreen()

        refreshToken()

        homeFragment = HomeFragment()
        bnvMain = findViewById(R.id.bnv_main)
        progressBar = findViewById(R.id.progressBar)
    }


    private fun refreshToken(){
        val sharedPreference = AppSharedPreference(application)
        val refreshToken = sharedPreference.get(AppConstants.REFRESH_TOKEN)
        val call = TokenRepository.refreshToken(application, RefreshTokenRequest(refreshToken))

//        val execute = call.execute()
//
//        if(execute.code() != 401){
//            val newToken = call.execute().body()
//
//            newToken?.refresh?.let { sharedPreference.insert(AppConstants.REFRESH_TOKEN, it) }
//            newToken?.access?.let { sharedPreference.insert(AppConstants.ACCESS_TOKEN, it) }
//
//            fetchDriver()
//
//        } else {
//            val intent = Intent(this@MainActivity, PhoneNumberLoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }


        call.enqueue(object : Callback<NewTokenResponse> {
            override fun onResponse(
                call: Call<NewTokenResponse>,
                response: Response<NewTokenResponse>
            ) {
                if(response.isSuccessful){
                    val newToken = response.body()

                    newToken?.refresh?.let { sharedPreference.insert(AppConstants.REFRESH_TOKEN, it) }
                    newToken?.access?.let { sharedPreference.insert(AppConstants.ACCESS_TOKEN, it) }

                    // get driver data after refresh token
                    fetchDriver()

                } else {
                    // when logging in is failed, navigate to login screen
                    val intent = Intent(this@MainActivity, PhoneNumberLoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<NewTokenResponse>, t: Throwable) {
                Log.d("TAG.....", "onFailure: ${t.toString()}")
            }

        })
    }

    private fun fetchDriver(){
        val driverViewModel = ViewModelProviders.of(this).get(DriverViewModel::class.java)
        driverViewModel.driver().observe(this){ driver ->

            // get data from for later use
            this.driver = driver

            // load home fragment after successfully fetched data
            loadFragment(homeFragment!!)

            // allow switch fragment after successfully fetched data
            switchFragment(bnvMain)

            // hide progressbar
            progressBar.visibility = ProgressBar.GONE
        }
    }

    private fun switchFragment(bnv: BottomNavigationView){
        bnv.setOnNavigationItemSelectedListener { item ->

            selectedFragment = when (item.itemId){
                R.id.nav_summary -> {
                    summaryFragment = summaryFragment ?: SummaryFragment()
                    bundle.putString("balance", driver.driverProfile?.totalBalance ?: "0")
                    bundle.putString("rating", driver.driverProfile?.totalRating ?: "0")

                    summaryFragment!!.arguments = bundle
                    summaryFragment!!
                }
                R.id.nav_profile -> {
                    driverProfileFragment = driverProfileFragment ?: DriverProfileFragment()
                    driverProfileFragment!!.driver = driver
                    driverProfileFragment!!
                }
                else -> homeFragment!!
            }

            loadFragment(selectedFragment)
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean{
        supportFragmentManager.beginTransaction().replace(R.id.fvc_main, fragment).commit()
        return true
    }

}