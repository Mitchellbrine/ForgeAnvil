package mc.Mitchellbrine.forgeAnvil.core.mod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import mc.Mitchellbrine.forgeAnvil.core.AnvilWriter;
import mc.Mitchellbrine.forgeAnvil.core.ForgeAnvil;
import mc.Mitchellbrine.forgeAnvil.error.IncorrectChoiceException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AnvilMod {

    private ArrayList<String> MODID = new ArrayList<String>();
    private ArrayList<String> NAME = new ArrayList<String>();
    private ArrayList<String> VERSION = new ArrayList<String>();
    public static ArrayList<String> ERROREDMODS = new ArrayList<String>();
    public static HashMap<String, Exception> errorHashMap = new HashMap<String, Exception>();
    public static String[] errorMessages = new String[] {"Looks like the mod maker f**ked something up! :D","#DontTellLex","#MaybeTellLex","Well the mod worked in Mincecraft!","The mod was a lie!","The mod is a lie, nothing is real!","Do you want to throw an error?","For the first time in forever, the mod crashed!","Let it work! Let it work! Come on! Let it work!","java.lang.SciSpoonException","java.lang.SciRageException","throw UpException();","It's all @kkaylium's fault! Go tweet her about it!","THE KKAYLIUM CONSPIRACY RULES OVERALL!","We crash what we must because we can!","The results are in. The mod failed. We weren't even testing for that.","#kkayliumconspiracy","This mod doesn't work past 1.2.5","#BecauseSoar1n","Exception up = new ExceptionThrowUp();","Poppy is not invisible","All hail the great emoticons! (/O.O)/","You wanted to exit the game right?","This error is JamCraft approved!","KKC...","KKFC...","~~Redeem this hug coupon at your nearest kkaylium. She won't take it from us.~~","java.lang.IdiotException","java.lang.YouAreAHorriblePersonException","throw TableException();","For more info, please consult nope.avi"};
    public static ArrayList<String> errorSplashes;


    public AnvilMod(String modid, String name) {
        buildErrorMessages();
        MODID.add(modid);
        NAME.add(name);
        VERSION.add("1.0 ( Or wasn't given D: )");
        FMLCommonHandler.instance().bus().register(this);
    }

    public AnvilMod(String modid, String name, String version) {
        buildErrorMessages();
        MODID.add(modid);
        NAME.add(name);
        VERSION.add(version);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SuppressWarnings("deprecation")
    public AnvilMod(String modid, int useMODID) {
        buildErrorMessages();
        if (useMODID == 1) {
            MODID.add(modid);
            NAME.add(modid);
            VERSION.add("1.0 ( Or wasn't given D: )");
        } else {
            try {
                throw new IncorrectChoiceException("Please say 1 or I will be mad!!! D:");
            } catch (IncorrectChoiceException ex) {
                createModFolder(modid);
                try {
                    PrintWriter pr = AnvilWriter.writer("ForgeAnvil/mod-properties/" + modid.toLowerCase() + ".properties");
                    Random random = new Random();
                    File errorSplash = new File("ForgeAnvil/errorSplashes.txt");
                    if (errorSplash.exists() && errorSplashes != null) {
                        pr.println(errorSplashes.get(random.nextInt(errorSplashes.size())));
                    } else {
                        pr.println(errorMessages[random.nextInt(errorMessages.length)]);
                    }
                    pr.println("~ ~ ~ ~ ~");
                    pr.println("");
                    ex.printStackTrace(pr);
                    pr.close();
                    ERROREDMODS.add(modid);
                    errorHashMap.put(modid, new IncorrectChoiceException());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FMLCommonHandler.instance().bus().register(this);
    }

    public AnvilMod(String modid, int useMODID, String version) {
        if (useMODID == 1) {
            MODID.add(modid);
            NAME.add(modid);
            VERSION.add(version);
        } else {
            try {
                throw new IncorrectChoiceException("Please say 1 or I will be mad!!! D:");
            } catch (IncorrectChoiceException ex) {
                createModFolder(modid);
                try {
                    PrintWriter pr = AnvilWriter.writer("ForgeAnvil/mod-properties/" + modid.toLowerCase() + ".properties");
                    Random random = new Random();
                    File errorSplash = new File("ForgeAnvil/errorSplashes.txt");
                    if (errorSplash.exists() && errorSplashes != null) {
                        pr.println(errorSplashes.get(random.nextInt(errorSplashes.size())));
                    } else {
                        pr.println(errorMessages[random.nextInt(errorMessages.length)]);
                    }
                    pr.println("~ ~ ~ ~ ~");
                    pr.println("");
                    ex.printStackTrace(pr);
                    pr.close();
                    ERROREDMODS.add(modid);
                    errorHashMap.put(modid,new IncorrectChoiceException());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FMLCommonHandler.instance().bus().register(this);
    }


    public static String barrier = "####################";
    public static String hashtag = "# ";

    private static void createModFolder(String modid) {
        File file2 = new File("ForgeAnvil/mods/"+modid+"/a.txt");
        file2.getParentFile().mkdir();
    }

    @SubscribeEvent
    public void worldLoaded(PlayerEvent.PlayerLoggedInEvent event) {
        if (ERROREDMODS.size() > 0) {
            ForgeAnvil.logger.info("");
            ForgeAnvil.logger.error("~~~~~~~~~~");
            ForgeAnvil.logger.error("Errored Mods:");
            event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "Errored Mods:"));
            for (int i = 0; i < ERROREDMODS.size(); i++) {
                ForgeAnvil.logger.error("Failed: Mod ID (" + ERROREDMODS.get(i) + "), Failure ("+errorHashMap.get(ERROREDMODS.get(i)).toString()+")");
                event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "Failed: Mod ID (" + ERROREDMODS.get(i) + "), " + EnumChatFormatting.DARK_RED + "Failure " + EnumChatFormatting.DARK_RED + "(" + EnumChatFormatting.DARK_RED + "" + errorHashMap.get(ERROREDMODS.get(i)).toString() + ")"));
            }
            ForgeAnvil.logger.error("~~~~~~~~~~");
            ForgeAnvil.logger.info("");
        }
    }

    public static void makeModProperties(String modid,String name, String version) {
        createModFolder(modid);
        try {
            PrintWriter wr = AnvilWriter.writer("ForgeAnvil/mod-properties/" + modid.toLowerCase() + ".properties");
            wr.println(barrier);
            wr.println(hashtag);
            wr.println(hashtag + name+":");
            wr.println(hashtag);
            wr.println(hashtag + "MOD ID:");
            wr.println(hashtag + modid);
            wr.println(hashtag);
            wr.println(hashtag + "MOD VERSION:");
            wr.println(hashtag + version);
            wr.println(hashtag);
            wr.println(barrier);
            wr.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void buildErrorMessages() {

        try {
            File file = new File("ForgeAnvil/errorSplashes.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("ForgeAnvil/errorSplashes.txt"))));
            String str;
            errorSplashes = new ArrayList<String>();
            while ((str = reader.readLine()) != null) {
                if (str.startsWith("-- ")) {
                    errorSplashes.add(str.substring(3));
                }
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            try {
                PrintWriter pr = AnvilWriter.writer("ForgeAnvil/errorSplashes.txt");
                for (int i = 0; i < errorMessages.length; i++) {
                    AnvilWriter.write(pr, "-- " + errorMessages[i]);
                }
                pr.close();
            } catch (IOException ex2) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
