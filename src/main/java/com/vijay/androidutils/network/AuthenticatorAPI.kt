/* $Id$ */
package com.vijay.androidutils.network

import android.app.Activity
import android.os.Parcelable

abstract class AuthenticatorAPI : Parcelable {

    abstract val zuid: String

    abstract val token: String

    abstract val isUserSignedIn: Boolean

    abstract val isDcHasPfx: Boolean

    abstract val dcPfx: String

    abstract val dcBD: String

    abstract fun onSessionExpired(openLogin: Boolean)

    abstract fun signIn(activity: Activity)
}
