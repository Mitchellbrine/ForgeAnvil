package mc.Mitchellbrine.forgeAnvil.core.aml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import mc.Mitchellbrine.forgeAnvil.core.ForgeAnvil;
import net.minecraft.launchwrapper.LaunchClassLoader;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class AML implements IFMLLoadingPlugin{

    public static AMLInst inst;

    public static ArrayList<String> names = new ArrayList<String>();
    public static HashMap<String,String> versions = new HashMap<String, String>();
    public static HashMap<String,String> authors = new HashMap<String, String>();

    public AML() {
    }

    private static void addModURL(String url) {
        try {
            ((LaunchClassLoader) AML.class.getClassLoader()).addURL(new URL(url));
            ForgeAnvil.logger.info("Loaded File: " + url);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    private static void addMod(File url) {
        try {
            ((LaunchClassLoader) AML.class.getClassLoader()).addURL(url.toURI().toURL());
            ForgeAnvil.logger.info("Loaded File: " + url);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        System.err.println(AMLCore.class.getName());
        return AMLCore.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static void load() {
        if (inst == null) {
            inst = new AMLInst();
            inst.scanForMods();
        }
    }


    private static class AMLInst {

        File mcDir = (File) FMLInjectionData.data()[6];
        File modsFolder = new File(mcDir,"ForgeAnvil/mods");

        private void scanForMods() {
            for (File file : modFiles()) {
                if (!file.getName().endsWith(".jar") && !file.getName().endsWith(".zip"))
                    continue;
                findModFile(file);
            }
        }

        private void findModFile(File file) {
            try {
                ZipFile zip = new ZipFile(file);
                System.err.println(zip.getName());
                ZipEntry e = zip.getEntry("anvil.info");
                if (e == null) e = zip.getEntry("anvil.info");
                if (e != null)
                    findInformation(zip.getInputStream(e));
                zip.close();
                ((LaunchClassLoader)AML.class.getClassLoader()).addURL(file.toURI().toURL());
            } catch (Exception e) {
                ForgeAnvil.logger.fatal("Failed to find anvil.info from " + file.getName());
                e.printStackTrace();
            }
        }

        private List<File> modFiles() {
            List<File> list = new LinkedList<File>();
            if (modsFolder.listFiles() != null) {
                list.addAll(Arrays.asList(modsFolder.listFiles()));
            }
            return list;
        }

        private void findInformation(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String str;
            String modName = "";
            String versionName = "";
            String authorName = "";
            while ((str = reader.readLine()) != null) {
                if (str.startsWith("name: ") || str.startsWith("name:")) {
                    if (str.startsWith("name: ")) modName = str.substring(6);
                    else modName = str.substring(5);
                } else if (str.startsWith("version: ") || str.startsWith("version:")) {
                    if (str.startsWith("version: ")) versionName = str.substring(9);
                    else versionName = str.substring(8);
                } else if (str.startsWith("author: ") || str.startsWith("author:")) {
                    if (str.startsWith("author: ")) authorName = str.substring(8);
                    else authorName = str.substring(7);
                } else if (str.startsWith("load: ") || str.startsWith("load:")) {
                    if (!str.contains("--")) {
                        if (str.startsWith("load: ")) addModURL(str.substring(6));
                        else addModURL(str.substring(5));
                    } else {
                        if (str.startsWith("load: ")) addModURL(str.substring(6,str.indexOf("---")));
                        else addModURL(str.substring(5,str.indexOf("---")));
                    }
                }
            }
            names.add(modName);
            versions.put(modName, versionName);
            authors.put(modName, authorName);

        }
    }

}
