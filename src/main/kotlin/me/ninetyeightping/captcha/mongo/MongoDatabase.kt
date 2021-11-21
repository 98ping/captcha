package me.ninetyeightping.captcha.mongo

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.ninetyeightping.captcha.Captcha
import org.bson.Document

class MongoDatabase {
    lateinit var client: MongoClient
    lateinit var database: MongoDatabase
    lateinit var completions: MongoCollection<Document>

    init {
        client = MongoClient(MongoClientURI(Captcha.instance.config.getString("uri")))
        database = client.getDatabase("Captcha")
        completions = database.getCollection("completions")
        println("Completed mongo initialization")
    }
}