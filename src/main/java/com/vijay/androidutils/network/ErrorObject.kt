/* $Id$ */
package com.vijay.androidutils.network

import androidx.annotation.StringDef

class ErrorObject {

    var reason: String
    var extraData: Any? = null

    constructor() {
        this.reason = UNKNOWN
    }

    constructor(reason: String) {
        this.reason = reason
    }

    constructor(reason: String, extraData: Any?) {
        this.reason = reason
        this.extraData = extraData
    }


    companion object {
        const val UNKNOWN = "UNKNOWN" //No i18N
        const val NO_CONNECTION = "NO_CONNECTION" //No i18N
        const val TIMEOUT = "TIMEOUT" //No i18N
        const val UNAUTHORIZED = "UNAUTHORIZED" //No i18N
        const val SESSION_EXPIRED = "SESSION_EXPIRED" //No i18N
        const val ACCOUNT_NOT_VERIFIED = "ACCOUNT_NOT_VERIFIED" //No i18N
        const val ACCOUNT_DEACTIVATED = "ACCOUNT_DEACTIVATED" //No i18N
        const val IP_RESTRICTED = "IP_RESTRICTED" //No i18N
        const val FEATURE_RESTRICTED = "FEATURE_RESTRICTED" //No i18N
        const val EDITOR_CHECKOUT = "EDITOR_CHECKOUT" //No i18N

        @StringDef(UNKNOWN, NO_CONNECTION, TIMEOUT, UNAUTHORIZED, SESSION_EXPIRED, ACCOUNT_NOT_VERIFIED, ACCOUNT_DEACTIVATED, IP_RESTRICTED, FEATURE_RESTRICTED, EDITOR_CHECKOUT)
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class Reason
    }
}