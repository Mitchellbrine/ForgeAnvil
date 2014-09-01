package mc.Mitchellbrine.forgeAnvil.core.aml;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import mc.Mitchellbrine.forgeAnvil.core.ForgeAnvil;
import net.minecraft.launchwrapper.LaunchClassLoader;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.io.IOUtils;

public class AML implements IFMLLoadingPlugin{

    private static ByteBuffer downloadBuffer = ByteBuffer.allocateDirect(1 << 23);

    public static AMLInst inst;

    public static ArrayList<File> modsLoaded = new ArrayList<File>();

    public static ArrayList<String> names = new ArrayList<String>();
    public static HashMap<String,String> versions = new HashMap<String, String>();
    public static HashMap<String,String> authors = new HashMap<String, String>();

    public AML() {
    }

    private static void addModPath(String url) {
        try {
            ((LaunchClassLoader) AML.class.getClassLoader()).addURL(new URL(url));
            modsLoaded.add(new File(url));
            AMLCore.logger.info("Loaded File: " + url);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    private static void addModURL(URL url) {
        try {
            File libFile = new File("ForgeAnvil/downloaded-mods/"+url.getPath().substring(url.getPath().lastIndexOf("/")));
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "AML Downloader");
            int sizeGuess = connection.getContentLength();
            AML.inst.download(connection.getInputStream(), sizeGuess, libFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void addMod(File url) {
        try {
            ((LaunchClassLoader) AML.class.getClassLoader()).addURL(url.toURI().toURL());
            modsLoaded.add(url);
            AMLCore.logger.info("Loaded File: " + url);
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
        File dModsFolder = new File(mcDir,"ForgeAnvil/downloaded-mods/");

        private void scanForMods() {
            AMLCore.logger.info("~~~~~~~~~~");
            for (File file : modFiles()) {
                if (!file.getName().endsWith(".jar") && !file.getName().endsWith(".zip"))
                    continue;
                findModFile(file);
            }
            AMLCore.logger.info("~~~~~~~~~~");
        }

        private void findModFile(File file) {
            try {
                ZipFile zip = new ZipFile(file);
                ZipEntry e = zip.getEntry("anvil.info");
                if (e == null) e = zip.getEntry("anvil.info");
                if (e != null)
                    findInformation(zip.getInputStream(e),file);
                zip.close();
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

        private List<File> dModFiles() {
            List<File> list = new LinkedList<File>();
            if (dModsFolder.listFiles() != null) {
                list.addAll(Arrays.asList(dModsFolder.listFiles()));
            }
            return list;
        }

        private void findInformation(InputStream inputStream,File file) throws IOException {
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
                    if (str.contains(",")) {
                        if (str.startsWith("load: ")) {
                            String[] mods = str.substring(6).split(",");
                            for (File files : dModFiles()) {
                                files.delete();
                            }
                            for (String mod : mods) {
                                if (!mod.startsWith("http") && !mod.startsWith("https")) {
                                    addModPath(mod);
                                } else {
                                    if (mod.endsWith("zip")) {
                                        addModURL(new URL(mod));
                                    } else if (mod.endsWith("jar")) {
                                        addModURL(new URL(mod));
                                    }
                                }
                            }
                        } else {
                            String[] mods = str.substring(5).split(",");
                            for (File files : dModFiles()) {
                                files.delete();
                            }
                            for (String mod : mods) {
                                if (!mod.startsWith("http") && !mod.startsWith("https")) {
                                    addModPath(mod);
                                } else {
                                    if (mod.endsWith("zip")) {
                                        addModURL(new URL(mod));
                                    } else if (mod.endsWith("jar")) {
                                        addModURL(new URL(mod));
                                    }
                                }
                            }
                        }
                    } else {
                        if (str.startsWith("load: ")) {
                            String[] mods = str.substring(6).split(",");
                            for (File files : dModFiles()) {
                                files.delete();
                            }
                            for (String mod : mods) {
                                if (!mod.startsWith("http") && !mod.startsWith("https")) {
                                    addModPath(mod);
                                } else {
                                    if (mod.endsWith("zip")) {
                                        addModURL(new URL(mod));
                                    } else if (mod.endsWith("jar")) {
                                        addModURL(new URL(mod));
                                    }
                                }
                            }
                        } else {
                            String[] mods = str.substring(5).split(",");
                            for (File files : dModFiles()) {
                                files.delete();
                            }
                            for (String mod : mods) {
                                if (!mod.startsWith("http") && !mod.startsWith("https")) {
                                    addModPath(mod);
                                } else {
                                    if (mod.endsWith("zip")) {
                                        addModURL(new URL(mod));
                                    } else if (mod.endsWith("jar")) {
                                        addModURL(new URL(mod));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            names.add(modName);
            versions.put(modName, versionName);
            authors.put(modName, authorName);
            addMod(file);
        }

        private void download(InputStream is, int sizeGuess, File target) throws Exception {
            try {
                byte[] buffer = new byte[4096];
                int n = - 1;

                OutputStream output = new FileOutputStream(target);
                while ( (n = is.read(buffer)) != -1)
                {

                    output.write(buffer, 0, n);
                }
                    output.close();


                ((LaunchClassLoader)AML.class.getClassLoader()).addURL(target.toURI().toURL());
                modsLoaded.add(target);
                AMLCore.logger.info("Loaded File: " + target);

                /*}
                else
                {
                    throw new RuntimeException(String.format("The downloaded file %s has an invalid checksum %s (expecting %s). The download did not succeed correctly and the file has been deleted. Please try launching again.", target.getName(), cksum, validationHash));
                }*/
            } catch (Exception e) {
                throw e;
            }
        }

    }

}
