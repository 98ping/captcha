package me.ninetyeightping.captcha.verification

import me.ninetyeightping.captcha.Captcha
import java.util.*

data class CaptchaModel(
    var uniqueId: UUID,
    var failed: Boolean
) {

    fun construct() : String {
        return Captcha.instance.gson.toJson(this)
    }

    fun push() {
        Captcha.instance.captchaHandler.saveModel(this)
    }
}