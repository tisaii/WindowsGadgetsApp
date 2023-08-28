package com.tisaii.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Tian
 * 命令行执行工具类
 */
public class SystemCommandUtil {

    public static final String WIN_TAG = "win";

    /**
     * 执行操作系统指令
     *
     * @param command 指令
     * @return 执行响应结果
     */
    public static String executeCommand(String command){
        try {
            StringBuilder result = new StringBuilder();
            ProcessBuilder pb;
            String os = System.getProperty("os.name");
            if(os.toLowerCase().contains(WIN_TAG)){
                pb = new ProcessBuilder().command("cmd", "/c", command);
            }else{
                pb = new ProcessBuilder().command("/bin/sh", "-c", command);
            }
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream is = p.getInputStream();
            //增强buffer reader, 可以实现依次读取一行数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null){
                result.append(line).append(System.lineSeparator());
            }
            p.waitFor();
            is.close();
            reader.close();
            p.destroy();
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行操作系统指令
     *
     * @param command 指令
     * @param path 指令执行的路径
     * @return 执行响应结果
     */
    public static String executeCommand(String command, String path){
        try {
            StringBuilder result = new StringBuilder();
            ProcessBuilder pb;
            String os = System.getProperty("os.name");
            if(os.toLowerCase().contains("win")){
                pb = new ProcessBuilder().command("cmd", "/c", command).directory(new File(path));
            }else{
                pb = new ProcessBuilder().command("/bin/sh", "-c", command).directory(new File(path));
            }
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
            InputStream is = p.getInputStream();
            //增强buffer reader, 可以实现依次读取一行数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
                result.append(line).append(System.lineSeparator());
            }
            p.waitFor();
            is.close();
            reader.close();
            p.destroy();
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
