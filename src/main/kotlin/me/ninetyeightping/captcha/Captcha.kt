package me.ninetyeightping.captcha

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.ninetyeightping.captcha.mongo.MongoDatabase
import me.ninetyeightping.captcha.verification.CaptchaHandler
import org.bukkit.plugin.java.JavaPlugin

class Captcha : JavaPlugin() {

    companion object {
        lateinit var instance: Captcha
    }

    val gson: Gson = GsonBuilder().serializeNulls().create()
    lateinit var mongoDatabase: MongoDatabase
    lateinit var captchaHandler: CaptchaHandler

    override fun onEnable() {
        saveDefaultConfig()
        instance = this

        mongoDatabase = MongoDatabase()
        captchaHandler = CaptchaHandler()

        server.pluginManager.registerEvents(me.ninetyeightping.captcha.bukkit.MenuListener(), this)


    }
}