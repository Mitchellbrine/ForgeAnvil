package mc.Mitchellbrine.forgeAnvil.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mc.Mitchellbrine.forgeAnvil.core.ForgeAnvil;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class NameColorEvent {

    @SubscribeEvent
    public void getName(PlayerEvent.NameFormat event) {
        if (ForgeAnvil.showPlayerColors) {
            if (ForgeAnvil.nameColors.get(event.entityPlayer.getGameProfile().getId().toString()) != null) {
                event.displayname = ForgeAnvil.nameColors.get(event.entityPlayer.getGameProfile().getId().toString()) + event.username;
            } else {
                System.err.println(event.entityPlayer.getGameProfile().getId().toString());
            }
        }
    }

}
