package mc.Mitchellbrine.forgeAnvil.config;

import cpw.mods.fml.client.config.GuiConfig;
import mc.Mitchellbrine.forgeAnvil.core.ForgeAnvil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class FAConfigGUI extends GuiConfig {

        public FAConfigGUI(GuiScreen parent) {
            super(parent, new ConfigElement(ForgeAnvil.config.getCategory("chat")).getChildElements(), "ForgeAnvil", false, false, GuiConfig.getAbridgedConfigPath(ForgeAnvil.config.toString()));
        }
}
