package mc.Mitchellbrine.forgeAnvil.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ChatEvent {

    @SubscribeEvent
    public void livingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;

            if (Minecraft.getMinecraft().thePlayer == player) {
                if (Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) {
                    List<EntityPlayer> players = event.entityLiving.worldObj.playerEntities;
                    if (!player.getEntityData().hasKey("hasTyped") || !player.getEntityData().getBoolean("hasTyped")) {
                        for (EntityPlayer player1 : players) {
                            player1.addChatComponentMessage(new ChatComponentTranslation("chat.user.open", player.getCommandSenderName()));
                        }
                        player.getEntityData().setBoolean("hasTyped",true);
                    }
                } else {
                    if (player.getEntityData().hasKey("hasTyped") && player.getEntityData().getBoolean("hasTyped")) {
                        player.getEntityData().setBoolean("hasTyped",false);
                    }
                }
            }
        }
    }

}
