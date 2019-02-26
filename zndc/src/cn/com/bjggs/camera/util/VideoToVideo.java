package cn.com.bjggs.camera.util;

import java.io.File;
import java.util.List;
/*
 * 视频转换
 */
public class VideoToVideo {
	public static void main(String[] args) {
				videoToVideo("D:/video/video.mp4",
						"D:/video/ffmpeg.exe",
						"D:/video/video.swf");
	}
	
	/**
	 * 参数
	 * veido_path : 视频位置
	 * ffmpeg_path : 转换程序
	 * picPath : 图片位置
	 * */
	public static boolean VideoToPicture(String veido_path,String ffmpeg_path, String picPath) {  
		File file = new File(veido_path);  
		if (!file.exists()) {  
			System.err.println("路径[" + veido_path + "]对应的视频文件不存在!");  
			return false;  
		} 	
		List<String> commands = new java.util.ArrayList<String>();  
		commands.add(ffmpeg_path);  
		commands.add("-i");  
		commands.add(veido_path);  
		commands.add("-y");  
		commands.add("-f");  
		commands.add("image2");  
		commands.add("-ss");  
		commands.add("20");//这个参数是设置截取视频多少秒时的画面  
		//commands.add("-t");  
		//commands.add("0.001");  
		commands.add("-s");  
		commands.add("700x525");  
		commands.add(picPath);  
	   try {  
			ProcessBuilder builder = new ProcessBuilder();  
			builder.command(commands);  
			builder.start();  
			System.out.println("截取成功");  
		   return true;  
	   	} catch (Exception e) {  
		   e.printStackTrace();  
		   return false;  
	    }  
	} 

	/**
	 * 将视频转为flv视频 
	 * */
	public static boolean videoToVideo(String veidopath,String ffmpegpath, String codcFilePath) { 
		
		File file = new File(veidopath);  
		if (!file.exists()) {  
			System.err.println("路径[" + veidopath + "]对应的视频文件不存在!");  
			return false;  
		} 	
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> commands = new java.util.ArrayList<String>();  
        commands.add(ffmpegpath); // 添加转换工具路径
        commands.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
        commands.add(veidopath); // 添加要转换格式的视频文件的路径
        commands.add("-qscale");     //指定转换的质量
        commands.add("6");
        commands.add("-ab");        //设置音频码率
        commands.add("64");
        commands.add("-ac");        //设置声道数
        commands.add("2");
        commands.add("-ar");        //设置声音的采样频率
        commands.add("22050");
        commands.add("-r");        //设置帧频
        commands.add("24");
        commands.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        commands.add(codcFilePath);  
	   try {  
			ProcessBuilder builder = new ProcessBuilder();  
			builder.command(commands);
	        builder.redirectErrorStream(true);
	        builder.start();
			System.out.println("转码成功！");  
		   return true;  
	   	} catch (Exception e) {  
		   e.printStackTrace();  
		   return false;  
	    }  
	}
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public static boolean deleteFile(String sPath) {  
	   boolean flag = false;  
	   File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}  
}
