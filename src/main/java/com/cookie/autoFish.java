package com.cookie;

import org.bukkit.plugin.java.JavaPlugin;

public class autoFish extends JavaPlugin {

    public static autoFish instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new Listeners() , this);
    }
}
