package mc.Mitchellbrine.forgeAnvil.core;

import java.io.*;

public class AnvilWriter {

    public static void makeClasses(){
            File file = new File("ForgeAnvil/disclaimer.txt");
            file.getParentFile().mkdir();
            File file2 = new File("ForgeAnvil/mod-properties/ForgeAnvil.properties");
            file2.getParentFile().mkdir();
    }

    /**
     * Only ForgeAnvil should use this version!
     * @param filepath
     * @return Your PrintWriter
     * @throws IOException
     */
    @Deprecated
    public static PrintWriter writer(String filepath) throws IOException {
        PrintWriter pr = new PrintWriter(filepath,"UTF-8");
        return pr;
    }

    /**
     * The method to write your files! DO NOT USE THE FILEPATH ONE!
     * @param modid
     * @param fileName
     * @return
     * @throws IOException
     */
    public static PrintWriter writer(String modid, String fileName) throws IOException {
        PrintWriter pr = new PrintWriter("ForgeAnvil/mods/" + modid + "/" + fileName,"UTF-8");
        return pr;
    }

    /**
     *
     * @param pr (Best to use writer method before hand)
     * @param line (What do you want to write to your class? Use \n for new line)
     * @return The PrintWriter again!
     * @throws IOException
     */
    public static PrintWriter write(PrintWriter pr, String line) throws IOException {
        pr.println(line);
        return pr;
    }

    /**
     * Best way to use:
     *
     * while ((str = readText(modid,filename) != null)) {
     *     //Use this here
     * }
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readText(String modid, String fileName) throws IOException {
        BufferedReader versionFile = new BufferedReader(new InputStreamReader(new FileInputStream(new File("ForgeAnvil/mods/" + modid + "/" + fileName))));
        return versionFile.readLine();
    }

}
