package me.jordanzimmitti.jz_night_mode

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator
import com.luckycatlabs.sunrisesunset.dto.Location
import im.delight.android.location.SimpleLocation
import me.jordanzimmitti.jz_date_time.JZTime
import me.jordanzimmitti.jz_date_time.JZTimeFormat
import java.text.SimpleDateFormat
import java.util.*

/** Kotlin Class JZNightMode
 *
 * Class That Handles The Device Theme Based On The Sunset And Sunrise
 *
 * @author Jordan Zimmitti
 *
 * @version 1.0.0
 */
class JZNightMode(private val context : Context,
                  var dayTheme        : Int = 0,
                  var nightTheme      : Int = 0) {

    // Define And Initializes Location Value//
    private val location = getLocation()

    // Define And Initializes String Values//
    private val timeZone = Calendar.getInstance().timeZone.id

    /**.
     * Function That Checks If It Is Night Time
     *
     * @return 'true' if it is night, 'false' if day
     */
    fun isNight(): Boolean {

        // When No Location Was Found//
        if (location == null) { getError("Location"); return false }

        // Define And Instantiates SimpleDateFormat Value//
        val dateFormat = SimpleDateFormat("HH:mm", Locale.US)

        // Define And Instantiates SunriseSunsetCalculator Value//
        val dayNight = SunriseSunsetCalculator(location, timeZone)

        // Gets The Current Time, Sunrise, And Sunset//
        val currentTime = JZTime.getCurrentTime(JZTimeFormat.MILITARY)
        val sunrise     = dayNight.getOfficialSunriseForDate(Calendar.getInstance())
        val sunset      = dayNight.getOfficialSunsetForDate(Calendar.getInstance())

        // Parses The Current Time, Sunrise, And Sunset//
        val parsedCurrentTime = dateFormat.parse(currentTime)
        val parsedSunrise     = dateFormat.parse(sunrise)
        val parsedSunset      = dateFormat.parse(sunset)

        // Returns True If Night, False If Day//
        return parsedCurrentTime.before(parsedSunrise) || parsedCurrentTime.after(parsedSunset)
    }

    /**.
     * Function That Gets The Time When The Sun Rises
     *
     * @return The sunrise time
     */
    fun getSunrise(): String {

        // When No Location Was Found//
        if (location == null) { getError("Location"); return "" }

        // Define And Instantiates SunriseSunsetCalculator Value//
        val dayNight = SunriseSunsetCalculator(location, timeZone)

        // Gets The Sunrise//
        val sunrise = dayNight.getOfficialSunriseForDate(Calendar.getInstance())

        // Returns The Sunrise Time//
        return "${sunrise.substring(1)} AM"
    }

    /**.
     * Function That Gets The Time When The Sun Sets
     *
     * @return The sunset time
     */
    fun getSunset(): String {

        // When No Location Was Found//
        if (location == null) { getError("Location"); return "" }

        // Define And Instantiates SunriseSunsetCalculator Value//
        val dayNight = SunriseSunsetCalculator(location, timeZone)

        // Gets The Sunset//
        val sunset = dayNight.getOfficialSunsetForDate(Calendar.getInstance())

        // Gets The Sunset Time In Standard Time//
        val hour   = JZTime.fullTimeToHour(sunset, JZTimeFormat.STANDARD)
        val minute = JZTime.fullTimeToMinute(sunset)

        // Returns The Sunset Time//
        return "$hour:$minute PM"
    }

    /**.
     * Function That Gets The Theme Based On The Time Of Day
     *
     * @return The day or night theme
     */
    fun getTheme(): Int {

        // When dayTheme Or nightTheme Was Not Initialized//
        if (dayTheme == 0 || nightTheme == 0) getError("No Input")

        // Returns The Day Or Night Theme//
        return if (isNight()) nightTheme else dayTheme
    }

    /**.
     * Function That Checks If The Location Permission Was Granted
     *
     * @return 'true' if The permission is granted, 'false' if not
     */
    private fun isPermissionGranted(): Boolean {

        // Define And Initializes Int Value//
        val desiredState = PackageManager.PERMISSION_GRANTED

        // Define And Initializes String Value//
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        // Returns True If The Permission Is Granted, False If Not//
        return ActivityCompat.checkSelfPermission(context, permission) == desiredState
    }

    /**.
     * Function That Shows The Appropriate Error When An Error Occurs
     *
     * @param [error] The error to show
     */
    private fun getError(error: String) {

        when (error) {

            // When The Error Is A Location Error//
            "Location" -> Toast.makeText(context, "Error: Can't Retrieve Location", Toast.LENGTH_LONG).show()

            // When The Error Is A Forgotten Input Error//
            "No Input" -> error("[dayTheme] or [nightTheme] was not initialized")
        }
    }

    /**.
     * Function That Gets The Devices Current Location
     *
     * @return The Devices Location
     */
    private fun getLocation(): Location? {

        // When Device Can Not Access Location//
        if (!isPermissionGranted()) { grantPermission(); return null }

        // Gets The Devices Location//
        val location = SimpleLocation(context)

        // Gets The Devices Coordinates//
        val latitude  = location.latitude
        val longitude = location.longitude

        // Returns The Devices Location//
        return Location(latitude, longitude)
    }

    /**.
     * Function That Asks The User To Grant Location Access
     *
     */
    private fun grantPermission() {

        // Define And Initializes Int Value//
        val desiredState = PackageManager.PERMISSION_GRANTED

        // Define And Initializes String Value//
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        // When User Needs To Grant Location Access//
        if (ContextCompat.checkSelfPermission(context, permission) != desiredState) {

            // Asks For Permission//
            ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), 0)
        }
    }
}