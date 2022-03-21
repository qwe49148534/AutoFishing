package com.cookie;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.InteractionHand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.plaf.nimbus.State;

public class Listeners implements Listener {

    private final autoFish main = autoFish.instance;

    // 在釣魚得時候事件
    @EventHandler
    public void onFishing(PlayerFishEvent event) {
        final Player player = event.getPlayer();

        // 如果在釣到魚之前收竿的話,則解除自動釣魚
        // 如果有autoFish.use的權限的話取消自動釣魚
        if (event.isCancelled() && !player.hasPermission("autoFish.use")) return;

        // 如果釣到魚餌或是釣到魚
        if (event.getState() == PlayerFishEvent.State.BITE || event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            //跑一個會一直重複循環的程式代碼
            new BukkitRunnable() {
                @Override
                public void run() {


                }
            }.runTaskLater(main , event.getState() == PlayerFishEvent.State.BITE ? 5 : 20);

        }

    }
    
    private InteractionHand getHand(Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD) ? InteractionHand.MAIN_HAND : player.getInventory().getItemInOffHand().getType().equals(Material.FISHING_ROD) ? InteractionHand.OFF_HAND : null;
    }

}
