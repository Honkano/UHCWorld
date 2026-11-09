/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.platform;

import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extension.platform.PlatformManager;

public enum Capability {
    GAME_HOOKS{

        @Override
        void initialize(PlatformManager platformManager, Platform platform) {
            platform.registerGameHooks();
        }

        @Override
        void unload(PlatformManager platformManager, Platform platform) {
        }
    }
    ,
    CONFIGURATION,
    USER_COMMANDS{

        @Override
        void initialize(PlatformManager platformManager, Platform platform) {
            platformManager.getCommandManager().register(platform);
        }

        @Override
        void unload(PlatformManager platformManager, Platform platform) {
            platformManager.getCommandManager().unregister();
        }
    }
    ,
    PERMISSIONS,
    WORLDEDIT_CUI,
    WORLD_EDITING;


    void initialize(PlatformManager platformManager, Platform platform) {
    }

    void unload(PlatformManager platformManager, Platform platform) {
    }
}

