package center.techostartup.raksadriver.view.driverprofile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import center.techostartup.raksadriver.R
import center.techostartup.raksadriver.data.local.AppSharedPreference
import center.techostartup.raksadriver.data.model.Driver
import center.techostartup.raksadriver.utils.AppConstants
import center.techostartup.raksadriver.view.phonenumberlogin.PhoneNumberLoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DriverProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DriverProfileFragment : Fragment() {

    lateinit var driver: Driver

    private lateinit var tvFullName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvNationalId: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvVehicleType: TextView
    private lateinit var tvPlateNumber: TextView

    private lateinit var btnLogout: Button

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
        setValue()
    }

    private fun initView(view: View) {
        tvFullName = view.findViewById(R.id.tv_full_name)
        tvUsername = view.findViewById(R.id.tv_username)
        tvNationalId = view.findViewById(R.id.tv_national_id)
        tvAddress = view.findViewById(R.id.tv_address)
        tvVehicleType = view.findViewById(R.id.tv_vehicle_type)
        tvPlateNumber = view.findViewById(R.id.tv_plate_number)

        btnLogout = view.findViewById(R.id.btn_log_out)

        btnLogout.setOnClickListener {
            val sharedPreference = AppSharedPreference(requireActivity().application)
            sharedPreference.remove(AppConstants.ACCESS_TOKEN)
            sharedPreference.remove(AppConstants.REFRESH_TOKEN)

            val intent = Intent(requireActivity(), PhoneNumberLoginActivity::class.java)
            startActivity(intent)

            requireActivity().finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setValue() {
        tvFullName.text = "${driver.firstName} ${driver.lastName}"
        tvUsername.text = driver.username
        tvNationalId.text = driver.driverProfile?.nationalId
        tvAddress.text = driver.driverProfile?.address
        tvVehicleType.text = driver.driverProfile?.vehicleType
        tvPlateNumber.text = driver.driverProfile?.vehiclePlateNumber
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DriverProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DriverProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}