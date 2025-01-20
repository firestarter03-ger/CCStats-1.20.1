//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.firestarter03.ccstats.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    public ModMenuIntegration() {
    }

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CCStatsConfig::createConfigScreen;
    }
}
