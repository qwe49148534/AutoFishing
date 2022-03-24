package com.cookie;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;


public class Listeners implements Listener {

    private final autoFish main = autoFish.instance;

    @EventHandler
    public void onSwing(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        final EquipmentSlot hand = event.getHand();

        if (hand == null)
            return;

        assert item != null;
        if (item.getType().equals(Material.FISHING_ROD) && hand.equals(EquipmentSlot.HAND)) {
            player.sendTitle("自動釣魚中" , "" , 1,20,1);
        }
    }

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
                    // 獲取玩家當前的手上的東西
                    InteractionHand interactionHand = getHand(player);
                    if (interactionHand == null) return;
                        doRight(player , interactionHand);
                }
            }.runTaskLater(main , event.getState() == PlayerFishEvent.State.BITE ? 5 : 20);

        }

    }

    public void doRight(Player player , InteractionHand interactionHand) {
        ServerPlayer serverPlayer = null;
        try {
            serverPlayer = (ServerPlayer)  player.getClass().getMethod("getHandle").invoke(player);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert serverPlayer != null;
        serverPlayer.gameMode.useItem(serverPlayer , serverPlayer.getLevel() , serverPlayer.getItemInHand(interactionHand) , interactionHand);
        serverPlayer.swing(interactionHand);
    }

    private InteractionHand getHand(Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD) ? InteractionHand.MAIN_HAND : player.getInventory().getItemInOffHand().getType().equals(Material.FISHING_ROD) ? InteractionHand.OFF_HAND : null;
    }

}
