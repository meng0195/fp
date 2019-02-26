package cn.com.bjggs.datas.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.core.util.PropsUtil;

public class DB4MySQL{
    
	private static final Log log = Logs.getLog(DB4MySQL.class);
	
	public static final String path = PropsUtil.getString("db.back.path");
	
	private static final String insPath = PropsUtil.getString("db.install.path");
	
	private static final String user = PropsUtil.getString("db.back.user");
	
	private static final String password = PropsUtil.getString("db.back.password");
	
	private static final String host = PropsUtil.getString("db.host.ip");
	
	private static final String back = "\" \" \"" + insPath + "bin\\mysqldump\" -h" + host + " -u" + user + " -p" + password + " ";
	
	private static final String restore = "\" \" \"" + insPath + "bin\\mysql\" -h" + host+ " -u" + user + " -p" + password + " ";
	
	private static final String name = PropsUtil.getString("db.back.name");
    /**
     * 备份单个数据库
     * @param dbName 数据库名称
     * @return 备份成功或者失败
     */
    public static boolean backup(){    
        InputStream in = null;
        InputStreamReader inReader = null;
        BufferedReader br = null;
        OutputStreamWriter writer = null;
        FileOutputStream fout = null;
        try {
            log.info(name + "开始备份!");
            // mysqldump的安装路径，支持带空格
            String cmd = back + name;
            // cmd命令在后台执行，没有命令窗口出现或者一闪而过的情况
            Process process = Runtime.getRuntime().exec("cmd /c start /b " + cmd);
            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。
            // 注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行   
            in = process.getInputStream();// 控制台的输出信息作为输入流
            inReader = new InputStreamReader(in, "utf8");// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码
            
            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            // 组合控制台输出信息字符串   
            br = new BufferedReader(inReader);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();
            
            // 要用来做导入用的sql目标文件：   
            fout = new FileOutputStream(path + name + Times.format("yyyyMMdd", new Date()) + ".sql");
            writer = new OutputStreamWriter(fout, "utf8");
            writer.write(outStr);
            // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免   
            writer.flush();
        } catch (Exception e) {
            log.error(name + "备份失败!",e);
            return false;
        } finally{
            // 别忘记关闭输入输出流
            try {
                in.close();
                inReader.close();
                br.close();
                writer.close();
                fout.close();
            } catch (Exception e) {
            	log.error(name + "备份失败!",e);
                return false;
            }
        }
        log.info(name + "备份成功!");
        return true;
    }
    
    /**
     * 还原单个数据库
     * @param dbName 数据库名称
     * @return 还原成功或者失败
     */
    public static boolean restore(String dbName){
        OutputStream out = null;
        BufferedReader br = null;
        OutputStreamWriter writer = null;
        
        try {
            log.info(dbName + "开始还原!");
            // mysql的安装路径，支持带空格
            String cmd = restore + name;
            // cmd命令在后台执行，没有命令窗口出现或者一闪而过的情况
            Process process = Runtime.getRuntime().exec("cmd /c start /b " + cmd);
            out = process.getOutputStream();//控制台的输入信息作为输出流
            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(path + dbName), "utf8"));
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();
            writer = new OutputStreamWriter(out, "utf8");
            writer.write(outStr);
            // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免   
            writer.flush();
        } catch (Exception e) {
            log.error(dbName + "还原失败!",e);
            return false;
        } finally {
            // 别忘记关闭输入输出流
            try {
                out.close();
                br.close();
                writer.close();
            } catch (IOException e) {
                log.error(dbName + "还原失败!",e);
                return false;
            }
        }
        log.info(dbName + "还原成功!");
        return true;
    }
}