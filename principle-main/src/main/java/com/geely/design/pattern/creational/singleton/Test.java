package com.geely.design.pattern.creational.singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 2019/7/11 下午3:24
 *
 * @author liangbingtian
 */
public class Test {

  public static void main(String[] args)
      throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//    LazySingleton lazySingleton = LazySingleton.getInstance();
//    Thread t1 = new Thread(new T());
//    Thread t2 = new Thread(new T());
//
//    t1.start();
//    t2.start();
//
//    System.out.println("Program end");

    //通过序列化和反序列化破坏单例
//    HungrySingleton instance = HungrySingleton.getInstance();
    EnumInstance instance = EnumInstance.getInstance();
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("singleton_file"));
    oos.writeObject(instance);

    File file = new File("singleton_file");
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
//    HungrySingleton newInstance = (HungrySingleton) ois.readObject();
    EnumInstance newInstance = (EnumInstance) ois.readObject();
    System.out.println(instance);
    System.out.println(newInstance);
    System.out.println(instance == newInstance);


    Class objectClass = EnumInstance.class;
    //虽然已经使用私有构造器使得对象无法从外部构造，但是我们可以使用反射把构造器的权限打开。
    Constructor constructor = objectClass.getDeclaredConstructor(String.class, int.class);
    constructor.setAccessible(true);
    EnumInstance enumInstance = (EnumInstance) constructor.newInstance("Geely", 666);
//    HungrySingleton instance = HungrySingleton.getInstance();
//    HungrySingleton newInstance = (HungrySingleton) constructor.newInstance();
//
//    System.out.println(instance == newInstance);
    enumInstance.printTest();

  }

}
