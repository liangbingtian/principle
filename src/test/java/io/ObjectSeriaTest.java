package io;

import io.entity.Student;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;

/**
 * Created by liangbingtian on 2020/2/18 9:54 下午
 */
public class ObjectSeriaTest {

  /**
   * 做对象的序列化
   * @throws IOException
   */
  @Test
  public void seriaOutputTest() throws IOException {
    String file = "demo/obj.dat";
    //1. 对象序列化
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
    Student student = new Student("10001", "张三", 20);
    oos.writeObject(student);
    oos.flush();
    oos.close();
  }

  /**
   * 做对象的反序列化
   * @throws IOException
   */
  @Test
  public void seriaInputTest() throws IOException, ClassNotFoundException {
    String file = "demo/obj.dat";
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
    Student student = (Student) ois.readObject();
    System.out.println(student);
    ois.close();
  }


}
