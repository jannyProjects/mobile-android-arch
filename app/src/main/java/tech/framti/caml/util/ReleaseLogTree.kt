package tech.framti.caml.util

import android.util.Log
import timber.log.Timber

class ReleaseLogTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        val crashlytics = Firebase.crashlytics
//
//        if (t != null) {
//            crashlytics.recordException(t)
//        } else {
//            crashlytics.log(message)
//        }
    }

    override fun isLoggable(tag: String?, priority: Int) =
        !(Log.DEBUG == priority || Log.VERBOSE == priority || Log.INFO == priority)
}
