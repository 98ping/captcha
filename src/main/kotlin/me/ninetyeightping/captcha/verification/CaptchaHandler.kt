package me.ninetyeightping.captcha.verification

import com.mongodb.client.model.Filters
import lombok.Getter
import me.ninetyeightping.captcha.Captcha
import org.bson.Document
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class CaptchaHandler {

    var comps = arrayListOf<CaptchaModel>()

    var pendingCaptchas = hashMapOf<Player, Inventory>()

    init {
        for (document in Captcha.instance.mongoDatabase.completions.find()) {
            comps.add(serialize(document))
        }
        println("Completed serialization of documents in mongo")
    }

    fun deserialize(captchaModel: CaptchaModel) : Document {
        return Document.parse(captchaModel.construct())
    }

    fun refetch() {
        for (document in Captcha.instance.mongoDatabase.completions.find()) {
            comps.add(serialize(document))
        }
    }

    fun fromPlayer(player: Player) : CaptchaModel? {
        for (model in comps) {
            if (model.uniqueId == player.uniqueId.toString()) return model;
        }
        return null
    }

    fun existsAndPassed(player: Player) : Boolean {
        return comps.contains(fromPlayer(player)) && !fromPlayer(player)!!.failed
    }

    fun exists(player: Player) : Boolean {
        return comps.contains(fromPlayer(player))
    }

    fun passPlayer(player: Player) {
        val captchaModel = CaptchaModel(player.uniqueId.toString(), false)
        captchaModel.push()
        refetch()
    }

    fun serialize(document: Document) : CaptchaModel {
        return Captcha.instance.gson.fromJson(document.toJson(), CaptchaModel::class.java)
    }

    fun saveModel(captchaModel: CaptchaModel) {
        Captcha.instance.mongoDatabase.completions.replaceOne(Filters.eq("uniqueId", captchaModel.uniqueId), deserialize(captchaModel))
        refetch()
    }


}
