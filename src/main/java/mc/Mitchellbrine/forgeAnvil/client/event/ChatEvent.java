package mc.Mitchellbrine.forgeAnvil.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mc.Mitchellbrine.forgeAnvil.core.ForgeAnvil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ChatEvent {

    @SubscribeEvent
    public void soundEvent(ServerChatEvent event) {
        if (ForgeAnvil.playChatSounds) {
            List<EntityPlayerMP> players = event.player.worldObj.playerEntities;
            for (EntityPlayerMP playerMP : players) {
                if (playerMP.getGameProfile().getId() != event.player.getGameProfile().getId()) {
                if (event.message.toLowerCase().contains(playerMP.getCommandSenderName().toLowerCase())) {
                    playerMP.worldObj.playSoundAtEntity(playerMP, "forgeanvil:chatmention", 1.0F, 1.0F);
                } else {
                    playerMP.worldObj.playSoundAtEntity(playerMP, "forgeanvil:chat", 1.0F, 1.0F);
                }
                }
            }
        }

    }

}
