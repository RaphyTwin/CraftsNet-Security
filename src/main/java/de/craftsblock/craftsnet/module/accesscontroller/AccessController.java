package de.craftsblock.craftsnet.module.accesscontroller;

import de.craftsblock.craftsnet.addon.Addon;

public class AccessController extends Addon {

    private static AccessController instance;

    @Override
    public void onLoad() {
        
        // Last thing to execute
        instance = this;
    }

    @Override
    public void onEnable() {


    }

    @Override
    public void onDisable() {

        // Last thing to execute
        instance = null;
    }

}
