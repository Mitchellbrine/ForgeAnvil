package mc.Mitchellbrine.forgeAnvil.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mc.Mitchellbrine.forgeAnvil.client.event.ChatEvent;
import mc.Mitchellbrine.forgeAnvil.client.event.NameColorEvent;
import mc.Mitchellbrine.forgeAnvil.core.aml.AML;
import mc.Mitchellbrine.forgeAnvil.core.mod.AnvilMod;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Mod(modid = "ForgeAnvil", version = "1.0", useMetadata=true)
public class ForgeAnvil extends AnvilMod {

    public static Logger logger = LogManager.getLogger("ForgeAnvil");

    public static HashMap<String,EnumChatFormatting> nameColors = new HashMap<String, EnumChatFormatting>();

    public static ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");

    public ForgeAnvil() {
        super("ForgeAnvil", 1,"1.0");
    }

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        AML.load();
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {

        logger.info("");
        logger.info("");
        logger.info("~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
        logger.info("Found " + AML.names.size() + " mods that use ForgeAnvil! The following mods were loaded: ");
        if (AML.names.size() > 0) {
            logger.info("~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
            for (int i = 0; i < AML.names.size(); i++) {
                logger.info("Mod Name: " + AML.names.get(i));
                if (AML.versions.get(AML.names.get(i)) != null) {
                    logger.info("Mod Version: " + AML.versions.get(AML.names.get(i)));
                }
                if (AML.authors.get(AML.names.get(i)) != null) {
                    logger.info("Mod Author: " + AML.authors.get(AML.names.get(i)));
                }
                logger.info("~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
            }
            logger.info("");
            logger.info("");
        } else {
            logger.info("~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
            logger.info("");
            logger.info("");
        }

        AnvilWriter.makeClasses();

        try {
            PrintWriter w = new PrintWriter("ForgeAnvil/disclaimer.txt", "UTF-8");
            w.println("                   ");
            w.println("                   ");
            w.println("~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
            w.println("FORGE ANVIL IS NOT A SUBSIDIARY OF MINECRAFT FORGE. ALL BUG REPORTS INVOLVING FORGE ANVIL SHOULD BE CONSULTED WITH THE FORGE ANVIL TEAM!");
            w.println("~ ~ ~");
            w.println("Lex, I put in this disclaimer to help you! I hope people learn to tell the difference!");
            w.println("~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
            w.println("                   ");
            w.println("                   ");
            w.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        MinecraftForge.EVENT_BUS.register(new NameColorEvent());
        MinecraftForge.EVENT_BUS.register(new ChatEvent());

        this.addDonorColor("2b00c656-72f7-3d5b-85c1-da3c09cac1e8",2);
        this.addDonorColor("bea5e0c485c4454da081e1eaae6895ee",2);

    }

    @Mod.EventHandler
    public void startedEvent(FMLPostInitializationEvent event) {
        logger.warn("                   ");
        logger.warn("                   ");
        logger.warn("~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
        logger.warn("FORGE ANVIL IS NOT A SUBSIDIARY OF MINECRAFT FORGE. ALL BUG REPORTS INVOLVING FORGE ANVIL SHOULD BE CONSULTED WITH THE FORGE ANVIL TEAM!");
        logger.warn("~ ~ ~");
        logger.warn("Lex, I put in this disclaimer to help you! I hope people learn to tell the difference!");
        logger.warn("~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
        logger.warn("                   ");
        logger.warn("                   ");
    }

    public void addDonorColor(String playerName, int level) {
        switch(level) {
            case 0: nameColors.put(playerName,EnumChatFormatting.DARK_AQUA); break;
            case 1: nameColors.put(playerName,EnumChatFormatting.GOLD); break;
            case 2: nameColors.put(playerName,EnumChatFormatting.DARK_RED); break;
        }
    }

    public void addDonorColor(String playerName, EnumChatFormatting formatting) {
        nameColors.put(playerName,formatting);
    }


    }
