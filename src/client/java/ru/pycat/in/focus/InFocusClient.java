package ru.pycat.in.focus;

import com.fox2code.foxloader.config.ConfigEntry;
import com.fox2code.foxloader.loader.ClientMod;
import com.fox2code.foxloader.loader.Mod;

public class InFocusClient extends Mod implements ClientMod {
    public static final InFocusConfig CONFIG = new InFocusConfig();

    @Override
    public void onInit() {
        // no
    }

    public static class InFocusConfig {
        @ConfigEntry(configName = "Enabled")
        public boolean isEnabled = true;
    }
}
