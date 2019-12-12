import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TransformUTF8 {

    public static void main(String[] args)
    {
        String[] path = readPath();
        TransformUTF8.convertEncoding("GBK", "UTF-8", path[0],
                path[1], "", new FileFilter()
                {
                    public boolean accept(File pathname)
                    {
                        return pathname.isDirectory() || pathname.getName().endsWith("css")|| pathname.getName().endsWith("js");
                    }

                });
    }

    private static void convertEncoding(String srcEncode, String targetEncode, String srcPath,
                                        String targetPath, String relativePath, FileFilter filter)
    {
        File srcFile = new File(srcPath);
        if (srcFile.isFile())
        {
            try
            {
                File targetFile = new File(targetPath+ File.separatorChar + srcFile.getName());
                System.out.println(targetFile.getName());
                if (!targetFile.exists())
                {
                    targetFile.createNewFile();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "GBK"));
                BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    writer.write(new String(line.getBytes(), targetEncode));
                    writer.newLine();
                }
                writer.append("/*日期*/");
                System.out.println(targetFile.getAbsolutePath());
                reader.close();
                writer.flush();
                writer.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (srcFile.isDirectory())
        {
            File[] files = srcFile.listFiles(filter);
            // 建立目标目录
            File targetFileDir = new File(targetPath + relativePath);
            if (!targetFileDir.exists())
            {
                targetFileDir.mkdirs();
            }
            for (File f : files)
            {
                convertEncoding(srcEncode, targetEncode, f.getAbsolutePath(), targetFileDir.getAbsolutePath(), f.getAbsolutePath().substring(srcPath.length()), filter);
            }
        }
    }

    public static String[] readPath(){
        String[] strings = new String[2];
        try {
            File f = new File("path.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);
            NodeList nl = doc.getElementsByTagName("path");

            System.out.println("srcPath:"+ doc.getElementsByTagName("srcPath").item(0).getFirstChild().getNodeValue());
            System.out.println("targetPath:"+ doc.getElementsByTagName("targetPath").item(0).getFirstChild().getNodeValue());
            strings[0] = doc.getElementsByTagName("srcPath").item(0).getFirstChild().getNodeValue();
            strings[1] = doc.getElementsByTagName("targetPath").item(0).getFirstChild().getNodeValue();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return  strings;
    }
}

