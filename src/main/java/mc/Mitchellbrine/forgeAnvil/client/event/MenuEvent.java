package mc.Mitchellbrine.forgeAnvil.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mc.Mitchellbrine.forgeAnvil.client.gui.GuiFAMenu;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;

public class MenuEvent {

    @SubscribeEvent
    public void guiEvent(GuiOpenEvent event) {
        if (event.gui != null) {
            if (event.gui.getClass() == GuiMainMenu.class) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiFAMenu());
                event.setCanceled(true);
            }
        }
    }

}
