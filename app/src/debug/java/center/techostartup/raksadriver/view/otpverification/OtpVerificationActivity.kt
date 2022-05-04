package center.techostartup.raksadriver.view.otpverification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import center.techostartup.raksadriver.R
import center.techostartup.raksadriver.data.local.AppSharedPreference
import center.techostartup.raksadriver.data.remote.repository.TokenRepository
import center.techostartup.raksadriver.data.remote.request.RefreshTokenRequest
import center.techostartup.raksadriver.data.remote.request.TokenRequest
import center.techostartup.raksadriver.utils.AppConstants
import center.techostartup.raksadriver.utils.LoadingDialog
import center.techostartup.raksadriver.view.base.BaseActivity
import center.techostartup.raksadriver.view.driverprofile.DriverViewModel
import center.techostartup.raksadriver.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mukesh.OtpView

class OtpVerificationActivity : BaseActivity() {
    // get reference of the firebase auth
    lateinit var auth: FirebaseAuth

    private lateinit var otpViewPhoneOtp: OtpView
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        loadingDialog = LoadingDialog(this)

        auth = FirebaseAuth.getInstance()

        // get storedVerificationId from the intent
        val storedVerificationId = intent.getStringExtra("storedVerificationId")

        otpViewPhoneOtp = findViewById(R.id.otp_view_phone_otp)

        // verify phone number otp
        otpViewPhoneOtp.setOtpCompletionListener { otp ->
            signInWithPhoneAuthCredential(
                PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(),
                    otp
                )
            )
            loadingDialog.startLoadingDialog()
        }
    }

    // verifies if the code matches sent by firebase
    // if success start the new activity in our case it is main Activity
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseToken = task.result.user?.getIdToken(false)?.result?.token

                    val driverViewModel = ViewModelProviders.of(this).get(DriverViewModel::class.java)
                    driverViewModel.phoneLogin(TokenRequest(firebaseToken!!)).observe(this) { token ->
                        Log.e("sign-in-successfully", "signInWithPhoneAuthCredential $token")
                        loadingDialog.dismissDialog()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        loadingDialog.dismissDialog()
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()

                    }
                }
            }

    }
}