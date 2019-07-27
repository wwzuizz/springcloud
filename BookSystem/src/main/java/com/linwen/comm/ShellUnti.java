package com.linwen.comm;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ShellUnti {
    public static String execShell(String shellPath, String... params) {
        StringBuilder command = new StringBuilder(shellPath).append(" ");
        for (String param : params) {
            command.append(param).append(" ");
        }

        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        StringBuilder sb = null;
        try {
            Process process = Runtime.getRuntime().exec(command.toString());
            process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));


            sb = new StringBuilder();
            String line;
            while ((line = bufrIn.readLine()) != null) {
                sb.append(line);
            }
            while ((line = bufrError.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            if (bufrIn != null) {
                try {
                    bufrIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufrError != null) {
                try {
                    bufrError.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
//        String result = execCmd("java -version", null);

        System.out.println(execShell("/home/linwen/1/git.sh"));
    }

    /**
     * 执行系统命令, 返回执行结果
     *
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static String execCmd(String cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }

        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }
}