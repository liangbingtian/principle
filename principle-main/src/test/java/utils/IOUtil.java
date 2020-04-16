package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liangbingtian on 2020/2/15 4:45 下午
 */
public class IOUtil {

  /**
   * 读取指定文件的内容，按照16进制输出到控制台
   * 并且每输出10个byte换行
   * @param fileName
   */
  public static void printHex(String fileName) throws IOException {
    FileInputStream in = new FileInputStream(fileName);
    int b;
    int i = 1;
    while ((b = in.read())!=-1) {
      //单位数之前补0
      if (b<=0xf) {
        System.out.print(0);
      }
      System.out.print(Integer.toHexString(b)+" ");
      if (i++%10==0){
        System.out.println();
      }
    }
    in.close();
  }

  /**
   * 读字节文件为什么要&0xff。因为byte类型为8位，int类型为32位。为了避免数据转化错误，通过&0xff将高24位进行清0
   * @param fileName
   * @throws IOException
   */
  public static void printHexByByteArray(String fileName)throws IOException {
    FileInputStream inputStream = new FileInputStream(fileName);
    byte[] buf = new byte[20 * 1024];
    /* 从inputstream中批量读取字节，放入到buf这个字节数组中,
     * 从第0个位置开始放，最多放buf.length个
     * 返回的是读到的字节个数
     */
//    int bytes = inputStream.read(buf, 0, buf.length);//一次性读完，说明字节数组足够大
//    int j = 1;
//    for (int i = 0; i<bytes; i++) {
//      if (buf[i] <= 0xf) {
//        System.out.print("0");
//      }
//      System.out.print(Integer.toHexString(buf[i])+" ");
//      if (j++%10==0) {
//        System.out.println();
//      }
//    }
    int bytes = 0;
    int j = 1;
    while ((bytes = inputStream.read(buf, 0, buf.length))!=-1) {
      for (int i = 0; i< bytes; i++) {
        System.out.print(Integer.toHexString(buf[i] & 0xff)+" ");
        if (j++%10==0) {
          System.out.println();
        }
      }
    }
  }


  public static void read() throws IOException {
    //如果文件不存在，则直接创建，如果存在，删除后创建,或者append为true的话则为追加
    FileOutputStream outputStream = new FileOutputStream("demo/out.dat", true);
    outputStream.write('A');//写出了'A'的低八位
    outputStream.write('B');
  }


}
