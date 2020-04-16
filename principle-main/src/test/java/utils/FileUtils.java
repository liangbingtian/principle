package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liangbingtian on 2020/2/11 9:14 下午
 * 列出file的一些常用操作比如过滤，遍历等操作
 */
public class FileUtils {

  /**
   * 列出指定目录下（包括其子目录）的所有文件
   * @param dir
   * @throws IOException
   */
  public static void listDirectory(File dir) throws IOException, IllegalAccessException {
    if (!dir.exists()) {
      throw new IllegalAccessException("目录:"+dir+"不存在.");
    }
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException(dir+"不是目录");
    }
    //列出直接的子名称，不包含其子
    String[] filenames = dir.list();
    for (String string : filenames) {
      System.out.println(string);
    }
    //返回的是直接子目录(文件)的抽象
    File[] files = dir.listFiles();
    //下面用一个递归操作去遍历
  }

  public static void copyFile(File srcFile, File destFile)throws IOException {
    if (!srcFile.exists()) {
      throw new IllegalArgumentException("文件:"+srcFile+"不存在");
    }
    if (!srcFile.isFile()) {
      throw new IllegalArgumentException(srcFile+"不是文件");
    }
    FileInputStream inputStream = new FileInputStream(srcFile);
    FileOutputStream outputStream = new FileOutputStream(destFile);
    byte[] buf = new byte[8*1024];
    int b;
    while ((b = inputStream.read(buf, 0, buf.length))!=-1) {
      outputStream.write(buf, 0, b);
      outputStream.flush();
    }
    inputStream.close();
    outputStream.close();
  }

  /**
   * 缓冲区拷贝文件
   * @param srcFile
   * @param destFile
   * @throws IOException
   */
  public static void copyFileByBuffer(File srcFile, File destFile)throws IOException {
    if (!srcFile.exists()) {
      throw new IllegalArgumentException("文件:"+srcFile+"不存在");
    }
    if (!srcFile.isFile()) {
      throw new IllegalArgumentException(srcFile+"不是文件");
    }
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
    int c;
    while ((c = bis.read())!=-1) {
      bos.write(c);
      bos.flush();
    }
    bis.close();
    bos.close();
  }

  //单字节拷贝
  public static void copyFileByByte(File srcFile, File destFile)throws IOException{
    if (!srcFile.exists()) {
      throw new IllegalArgumentException("文件:"+srcFile+"不存在");
    }
    if (!srcFile.isFile()) {
      throw new IllegalArgumentException(srcFile+"不是文件");
    }
    FileInputStream fis = new FileInputStream(srcFile);
    FileOutputStream fos = new FileOutputStream(destFile);
    int c;
    while ((c = fis.read())!=-1) {
      fos.write(c);
      fos.flush();
    }
    fis.close();
    fos.close();
  }

}
