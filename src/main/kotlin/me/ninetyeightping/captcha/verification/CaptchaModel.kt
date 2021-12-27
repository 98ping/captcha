package me.ninetyeightping.captcha.verification

import com.mongodb.client.model.UpdateOptions
import me.ninetyeightping.captcha.Captcha
import org.bson.Document
import java.util.*

data class CaptchaModel(
    var uniqueId: String,
    var failed: Boolean
) {

    fun construct() : String {
        return Captcha.instance.gson.toJson(this)
    }

    fun push() {
        var toPushStarter: Document =   Document.parse(construct())
        toPushStarter.remove("_id")

        val query = Document("_id", uniqueId)
        val toUpdate = Document("\$set", toPushStarter)

        Captcha.instance.mongoDatabase.completions.updateOne(query, toUpdate, UpdateOptions().upsert(true))

    }
}