package com.linwen.comm.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by linwen on 18-12-13.
 */
public class FileTool {

    /**
     * 创建文件
     *
     * @param filePath
     */
    public static void mkdirsFile(File filePath) {
        if (!filePath.exists()) {
            Runtime runtimer = Runtime.getRuntime();
            filePath.mkdirs();
            String command = "chmod 777 " + filePath.getPath();
            try {
                Process process = runtimer.exec(command);
                process.waitFor();
                int existValue = process.exitValue();
                if (existValue != 0) {
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 写入bytes
     *
     * @param path
     * @param bytes
     */
    public static void byteOutPut(String path, byte[] bytes) {
        try {
            //1.实例化FileOutputStream对象
            FileOutputStream fos = new FileOutputStream(path, false);
            //2.实例化BuffereOutputStream 对象
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            //3.实例化DataOutputStream对象
            DataOutputStream dos = new DataOutputStream(bos);
            //4.使用dos来写文件
            dos.write(bytes);
            //关闭流
            dos.close();
            bos.close();
            fos.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readJson(String path) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);

            FileChannel channel = fis.getChannel();
            ByteBuffer byteBuffer = null;
            byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
            }
            return new String(byteBuffer.array());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean mvFile(String cachePath, String filePath) {
        File startFile = new File(cachePath);
        File tmpFile = new File(filePath);//获取文件夹路径
        return startFile.renameTo(tmpFile);
    }
}
