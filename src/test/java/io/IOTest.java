package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.Test;
import utils.FileUtils;
import utils.IOUtil;

/**
 * Created by liangbingtian on 2020/2/6 10:20 下午
 */
public class IOTest {

  @Test
  public void encodeDemoTest() throws UnsupportedEncodingException {
    String s = "慕课ABC";
    byte[] bytes = s.getBytes();//转换成字节序列默认使用的项目编码是gbk
    System.out.println("使用默认编码打印");
    for (byte bt : bytes) {
      //byte可以转为int。应该是属于范围扩大了。
      System.out.print(Integer.toHexString(bt & 0xff) + " ");
    }
    System.out.println();
    //使用gbk编码
    byte[] bytes1 = s.getBytes("gbk");
    for (byte bt1 : bytes1) {
      System.out.print(Integer.toHexString(bt1 & 0xff) + " ");
    }

    //使用utf-16be编码。中文占用两个字节，英文占用两个字节
    System.out.println();
    byte[] bytes2 = s.getBytes(StandardCharsets.UTF_16BE);
    for (byte bt2 : bytes2) {
      System.out.print(Integer.toHexString(bt2 & 0xff) + " ");
    }

    /*
     * 当你的字节序列是某种编码时,这个时候想把字节序列变成字符串
     * ，也需要用这种编码方式，否则会出现乱码
     */
    System.out.println();
    //就比如说上边这种字节是gbk格式编码的。转换成字符串也要指定成gbk格式编码。否则就会乱码
    String str1 = new String(bytes1);
    System.out.println(str1);
    String str2 = new String(bytes1, "gbk");
    System.out.println(str2);
  }

  @Test
  public void fileDemo() {
    File file = new File("/Users/liangbingtian/Downloads/loginshell/25的副本.txt");
    if (!file.exists()) {
      boolean result = file.mkdir();
      System.out.println(result);
    }
  }

  @Test
  public void randomAccessFileDemo() throws IOException {
    File demo = new File("demo");
    if (!demo.exists()) {
      demo.mkdir();
    }
    File file = new File(demo, "raf.dat");
    if (!file.exists()) {
      file.createNewFile();
    }

    RandomAccessFile raf = new RandomAccessFile(file, "rw");
    //指针的位置
    System.out.println(raf.getFilePointer());
    //写一个字节，写字母A的后8位，也有可能后8位也能够表示A了。
    raf.write('A');
    System.out.println(raf.getFilePointer());
    raf.write('B');

    //最大的整数,4个字节32位
    int i = 0x7fffffff;
    //依次写入高八位，中8位，低8位
    raf.write(i >>> 24);
    raf.write(i >>> 16);
    raf.write(i >>> 8);

    System.out.println(raf.getFilePointer());

    //直接写一个int
    raf.writeInt(i);

    String s = "中";
    byte[] gbk = s.getBytes("gbk");
    raf.write(gbk);
    System.out.println(raf.length());

    //读文件，必须把指针移到头部
    raf.seek(0);
    //一次性读取，把文件中的内容都读取到字节数组中
    byte[] buf = new byte[(int) raf.length()];
    raf.read(buf);
    //打印的是ASCII码
    System.out.println(Arrays.toString(buf));
    for (byte b : buf) {
      System.out.println(Integer.toHexString(b & 0xff) + " ");
    }
  }

  @Test
  public void IOUtilTest() {
    try {
      IOUtil.printHexByByteArray(
          "/Users/liangbingtian/Desktop/principle/src/test/java/utils/FileUtils.java");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void FileOutputTest() throws IOException {
    //如果文件不存在，则直接创建，如果存在，删除后创建,或者append为true的话则为追加
    FileOutputStream outputStream = new FileOutputStream("demo/out.dat");
    int a = 10;//write只能写八位,那么写一个int需要写四次每次8位
    outputStream.write(a >>> 24);
    outputStream.write(a >>> 16);
    outputStream.write(a >>> 8);
    outputStream.write(a);
    //写一个汉字
    byte[] aa = "中国".getBytes(StandardCharsets.UTF_8);
    outputStream.write(aa);
    outputStream.close();

    IOUtil.printHex("demo/out.dat");
  }

  /**
   * DataOutputSream创建文件
   *
   * @throws IOException
   */
  @Test
  public void DosDemoTest() throws IOException {
    String file = "demo/dos.dat";
    DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
    dos.writeInt(10);
    dos.writeInt(-10);
    dos.writeLong(10L);
    dos.writeDouble(10.5);
    //采用utf-8编码写出
    dos.writeUTF("中国");
    //采用utf-16be写出
    dos.writeChars("中国");
  }

  /**
   * DataInputStream写入文件
   *
   * @throws IOException
   */
  @Test
  public void DisDemoTest() throws IOException {
    String file = "demo/dos.dat";
    DataInputStream dis = new DataInputStream(new FileInputStream(file));
    int i = dis.readInt();
    System.out.println(i);
    i = dis.readInt();
    System.out.println(i);
    long l = dis.readLong();
    System.out.println(l);
    System.out.println(dis.readDouble());
    System.out.println(dis.readUTF());
  }

  /**
   * 拷贝文件速度测试
   *
   * @throws IOException
   */
  @Test
  public void copyFileTest() throws IOException {
    String file = "/Users/liangbingtian/Downloads/fileCopyTest/111.xml";
    long start = System.currentTimeMillis();
    FileUtils.copyFileByByte(new File(file),
        new File("/Users/liangbingtian/Downloads/fileCopyTest/拷贝之后.xml"));
    long end = System.currentTimeMillis();
    System.out.println(end - start);
  }

  /**
   * 字节流和二进制流之间的互转。
   */
  @Test
  public void IsrAndOswTest() throws IOException {
    FileInputStream inputStream = new FileInputStream(
        "/Users/liangbingtian/Downloads/fileCopyTest/char的测试.txt");
    FileOutputStream outputStream = new FileOutputStream("/Users/liangbingtian/Downloads/fileCopyTest/char的测试1.txt");
    InputStreamReader charInputStream = new InputStreamReader(inputStream);//默认编码为默认项目的编码
    OutputStreamWriter charOutputStream = new OutputStreamWriter(outputStream);
//    int c;
//    while ((c=charInputStream.read())!=-1) {
//      System.out.print((char) c);
//    }
    char[] buffer = new char[8 * 1024];
    int c;
    /**
     * 批量读取，放入buffer这个字符数组,从第0个开始放置，最多放buffer.length个。
     */
    while ((c = charInputStream.read(buffer, 0, buffer.length)) != -1) {
      String s = new String(buffer, 0, c);
      System.out.print(s);
      charOutputStream.write(buffer, 0, c);
      charOutputStream.flush();
    }

    charInputStream.close();
    charOutputStream.close();

  }

  /**
   * 直接构造字符读写流
   */
  @Test
  public void FileReaderAndWriterTest() throws IOException {
    FileReader fileReader = new FileReader("/Users/liangbingtian/Downloads/fileCopyTest/char的测试.txt");
    FileWriter fileWriter = new FileWriter("/Users/liangbingtian/Downloads/fileCopyTest/char的测试1.txt");
    char[] buffer = new char[2056];
    int c;
    while ((c = fileReader.read(buffer, 0, buffer.length))!=-1){
      fileWriter.write(buffer, 0, c);
      fileWriter.flush();
    }
    fileReader.close();
    fileWriter.close();
  }

  /**
   * bufferReader, bufferWriter, PrintWriter对文件进行读写操作
   */
  @Test
  public void BrAndBwOrPwTest() throws IOException{
    //或者直接构造成FileReader
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
        new FileInputStream("/Users/liangbingtian/Downloads/fileCopyTest/char的测试.txt")
    ));
    //或者直接构造成FileWriter
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/liangbingtian/Downloads/fileCopyTest/char的测试1.txt"));
    //再构造FileWriter的时候可以直接使用这种构造方法更简单
    PrintWriter printWriter = new PrintWriter("/Users/liangbingtian/Downloads/fileCopyTest/char的测试1.txt");
    PrintWriter printWriter1 = new PrintWriter(new FileOutputStream("/Users/liangbingtian/Downloads/fileCopyTest/char的测试1.txt"), true);
    String line;
    while ((line = bufferedReader.readLine())!=null) {
      System.out.println(line);//一次读一行，并不能识别换行符
//      方案一
//      bufferedWriter.write(line);
//      //要单独的写出换行操作
//      bufferedWriter.newLine();//换行操作
//      bufferedWriter.flush();
      printWriter.println(line);//直接换行
      printWriter.flush();
    }
    bufferedReader.close();
    bufferedWriter.close();
    printWriter.close();
  }

}