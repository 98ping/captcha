package me.ninetyeightping.captcha.bukkit

import me.ninetyeightping.captcha.Captcha
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CaptchaMenu(itemStack: ItemStack, slot: Int) {
    val inventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&eClick On The &bDiamond")).also {
        it.setItem(slot, itemStack)
    }

    fun openMenu(player: Player) {
        Captcha.instance.captchaHandler.pendingCaptchas[player] = inventory
        player.openInventory(inventory)
    }

}