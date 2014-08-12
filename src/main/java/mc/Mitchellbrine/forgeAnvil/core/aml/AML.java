package mc.Mitchellbrine.forgeAnvil.core.aml;

import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AML implements IFMLLoadingPlugin, IFMLCallHook{

    public static AMLInst inst;

    public static ArrayList<String> names = new ArrayList<String>();
    public static HashMap<String,String> versions = new HashMap<String, String>();
    public static HashMap<String,String> authors = new HashMap<String, String>();

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() throws Exception {
        load();
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
        File modsFolder = new File(mcDir,"mods");

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
                ZipEntry e = zip.getEntry("anvil.info");
                if (e == null) e = zip.getEntry("anvil.info");
                if (e != null)
                    findInformation(zip.getInputStream(e));
                zip.close();
            } catch (Exception e) {
                System.err.println("Failed to find anvil.info from " + file.getName());
                e.printStackTrace();
            }
        }

        private List<File> modFiles() {
            List<File> list = new LinkedList<File>();
            list.addAll(Arrays.asList(modsFolder.listFiles()));
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
                }
            }
            names.add(modName);
            versions.put(modName, versionName);
            authors.put(modName, authorName);
        }
    }

}
