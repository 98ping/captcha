package me.ninetyeightping.captcha.bukkit

import me.ninetyeightping.captcha.Captcha
import me.ninetyeightping.captcha.utility.ItemBuilder
import net.minecraft.server.v1_8_R3.ItemBanner
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.E

class MenuListener : Listener {

    @EventHandler
    fun join(event: PlayerJoinEvent) {
        val player = event.player
        if (!Captcha.instance.captchaHandler.existsAndPassed(player)) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Captcha.instance, {
                CaptchaMenu(
                    ItemBuilder.of(Material.DIAMOND).name("&bClick Me!").build(),
                    ThreadLocalRandom.current().nextInt(9)
                ).openMenu(player)
            }, 20L)
        }
    }

    @EventHandler
    fun close(event: InventoryCloseEvent) {
        val player = event.player
        val inventory1 = event.inventory

        if (Captcha.instance.captchaHandler.pendingCaptchas.containsKey(player)) {
            val inventory = Captcha.instance.captchaHandler.pendingCaptchas[player]
            if (inventory != null) {
                if (inventory.title.equals(inventory1.title)) {
                    Captcha.instance.captchaHandler.pendingCaptchas.remove(player)
                }
            }
        }

        @EventHandler
        fun checkIfPassedOrNot(event: InventoryCloseEvent) {
            val player = event.player as Player
            val inventory1 = event.inventory

            if (Captcha.instance.captchaHandler.pendingCaptchas.containsKey(player)) {
                val inventory = Captcha.instance.captchaHandler.pendingCaptchas[player]
                if (inventory != null) {
                    if (inventory.title.equals(inventory1.title)) {
                        if (!Captcha.instance.captchaHandler.existsAndPassed(player)) {
                            player.kickPlayer("${ChatColor.RED}Failed the captcha!")
                        }
                    }
                }
            }
        }


        @EventHandler
        fun click(event: InventoryClickEvent) {
            val player = event.whoClicked as Player
            val inventory1 = event.inventory
            val clicked = event.currentItem

            if (Captcha.instance.captchaHandler.pendingCaptchas.containsKey(player)) {
                val inventory = Captcha.instance.captchaHandler.pendingCaptchas[player]
                if (inventory != null) {
                    if (inventory.title.equals(inventory1.title)) {
                        if (clicked.type == Material.DIAMOND) {
                            player.sendMessage("${ChatColor.YELLOW}You have passed the captcha")
                            Captcha.instance.captchaHandler.passPlayer(player)

                            player.closeInventory()
                        } else {
                            return
                        }
                    }

                }
            }
        }
    }
}