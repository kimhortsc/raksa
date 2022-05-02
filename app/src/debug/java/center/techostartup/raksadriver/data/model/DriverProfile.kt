package center.techostartup.raksadriver.data.model

import com.google.gson.annotations.SerializedName

data class DriverProfile(
    @SerializedName("national_id")
    val nationalId: String?,

    @SerializedName("vehicle_type")
    val vehicleType: String?,

    @SerializedName("vehicle_model")
    val vehicleModel: String?,

    @SerializedName("vehicle_color")
    val vehicleColor: String?,

    @SerializedName("vehicle_plate_location")
    val vehiclePlateLocation: String?,

    @SerializedName("vehicle_plate_number")
    val vehiclePlateNumber: String?,

    @SerializedName("address")
    val address: String?,

    @SerializedName("city")
    val city: String?,

    @SerializedName("smart_box_id")
    val smartBoxId: String?,

    @SerializedName("created_date")
    val createdDate: String?,

    @SerializedName("driver_status")
    val driverStatus: String?,

    @SerializedName("driver_status_code")
    val driverStatusCode: String?,

    @SerializedName("driver_type")
    val driverType: String?,

    @SerializedName("is_active")
    val active: Boolean?,

    @SerializedName("total_deposit")
    val totalDeposit: String?,

    @SerializedName("total_balance")
    val totalBalance: String?,

    @SerializedName("total_rating")
    val totalRating: String?,

    @SerializedName("total_rating_count")
    val totalRatingCount: String?,

    @SerializedName("commission_type_code")
    val commissionTypeCode: String?,

    @SerializedName("commission_type")
    val commissionType: String?,

    @SerializedName("commission_value")
    val commissionValue: String?
)