package com.frankuzi.webviewapplication.presentation.utils

import android.os.Build
import com.frankuzi.webviewapplication.BuildConfig
import java.util.Locale

class EmulatorChecker {
    fun isEmulator(): Boolean {
        if (BuildConfig.DEBUG)
            return false

        val phoneModel = Build.MODEL
        val buildProduct = Build.PRODUCT
        val buildHardware = Build.HARDWARE
        return (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware == "goldfish"
                || Build.BRAND.contains("google")
                || buildHardware == "vbox86"
                || buildProduct == "sdk"
                || buildProduct == "google_sdk"
                || buildProduct == "sdk_x86"
                || buildProduct == "vbox86p"
                || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
                || buildHardware.lowercase(Locale.getDefault()).contains("nox")
                || buildProduct.lowercase(Locale.getDefault()).contains("nox"))
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || ("google_sdk" == buildProduct)
    }
}