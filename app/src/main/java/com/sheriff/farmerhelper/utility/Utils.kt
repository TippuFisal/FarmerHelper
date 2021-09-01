package com.sheriff.farmerhelper.utility

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sheriff.farmerhelper.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


typealias OnPositiveButton = () -> Unit
typealias OnNegativeButton = () -> Unit

class Utils {

    companion object {

        /**
         * Is Connected - Internet check
         */
        fun isConnected(context: Context): Boolean {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    @Suppress("DEPRECATION")
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }

        /**
         * Single button Alert dialog with title
         *
         * @param activity
         * @param title
         * @param message
         * @param positiveBtnText
         * @param onPositiveButton
         */
        fun singleButtonDialogWithTitle(
                activity: Activity,
                title: String,
                message: String,
                positiveBtnText: String,
                onPositiveButton: OnPositiveButton,
        ) {
            val dialog = MaterialAlertDialogBuilder(
                    activity,
                    R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
            )
                    .setTitle(title)
                    .setCancelable(false)
                    .setMessage(message)
                    .setPositiveButton(positiveBtnText) { _, _ ->
                        onPositiveButton.invoke()
                    }
                    .show()
        }

        /**
         * Double button Alert dialog with title
         *
         * @param activity
         * @param title
         * @param message
         * @param positiveBtnText
         * @param negativeBtnText
         * @param onPositiveButton
         * @param onNegativeButton
         */
        fun doubleButtonDialogWithTitle(
                activity: Activity,
                title: String,
                message: String,
                positiveBtnText: String,
                negativeBtnText: String,
                onPositiveButton: OnPositiveButton,
                onNegativeButton: OnNegativeButton,
        ) {
            val dialog = MaterialAlertDialogBuilder(
                    activity,
                    R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
            )
                    .setTitle(title)
                    .setCancelable(false)
                    .setMessage(message)
                    .setPositiveButton(positiveBtnText) { _, _ ->
                        onPositiveButton.invoke()
                    }
                    .setNegativeButton(negativeBtnText) { _, _ ->
                        onNegativeButton.invoke()
                    }
                    .show()
        }


        fun doubleButtonDialog(
            activity: Activity,
            message: String,
            positiveBtnText: String,
            negativeBtnText: String,
            onPositiveButton: OnPositiveButton,
            onNegativeButton: OnNegativeButton,
        ) {
            val dialog = MaterialAlertDialogBuilder(
                activity,
                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
            )
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(positiveBtnText) { _, _ ->
                    onPositiveButton.invoke()
                }
                .setNegativeButton(negativeBtnText) { _, _ ->
                    onNegativeButton.invoke()
                }
                .show()

        }


        /**
         * Toast
         */
        fun toast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        /**
         * Log Debug
         */
        fun log(TAG: String, message: String) {
            Log.d(TAG, message)
        }

        /**
         * setImage
         */
        fun setImage(imageView: ImageView, imgUrl: String, context: Context) {
            Glide.with(context).load(imgUrl).into(imageView)
        }

        /**
         * Hide keyboard
         */
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * Show keyboard
         */
        fun showKeyboard(activity: Activity) {
            val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        /**
         * Move activity forward anim
         */
        fun moveActivityForwardAnim(activity: Activity) {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        /**
         * Move activity back anim
         */
        fun moveActivityBackAnim(activity: Activity) {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        /**
         * ConvertToCustomDateFormat
         */
        fun convertToCustomDateFormat(dateStr: String?): String {
            val utc = TimeZone.getTimeZone("UTC")
            val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
            val destFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a")
            sourceFormat.timeZone = utc
            val convertedDate = sourceFormat.parse(dateStr)
            return destFormat.format(convertedDate)
        }

        /**
         * ConvertToCustomFormatCalendar
         */
        fun convertToCustomFormatCalendar(dateStr: String?): String {
            val utc = TimeZone.getTimeZone("UTC")
            val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
            val destFormat = SimpleDateFormat("d MMM yyyy")
            sourceFormat.timeZone = utc
            val convertedDate = sourceFormat.parse(dateStr)
            return destFormat.format(convertedDate)
        }

        fun applyDynamicButtonColor(view: View, color: String,radius:Float) {
            try {
                val gd1 = GradientDrawable()
                gd1.cornerRadius = radius
                gd1.setColor(Color.parseColor(color))
                view.background = gd1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun isValidEmail(email: CharSequence?): Boolean {
            var isValid = true
            val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            if (!matcher.matches()) {
                isValid = false
            }
            return isValid
        }

    }
}