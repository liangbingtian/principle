package utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by liangbingtian on 2020/2/22 9:49 下午
 */
public class ClassUtil {

  /**
   * 打印类的信息，包括类的成员函数，成员变量
   * @param object 该对象所属类的信息
   */
  public static void printClassMessage(Object object) {
    //要获取类的信息 首先要获取类的类类型
    Class c = object.getClass();//传递的是哪个子类的对象 c就是该对象类的类类型
    System.out.println("类的名称是:"+c.getName());
    //在java中。万物皆对象。方法也是。方法是Method的对象
    /**
     * Method类，方法对象
     * 一个成员方法就是一个Method对象
     * getMehtods()方法获取的是所有的public函数，包括父类继承而来的
     * getDeclaredMethods()获取的是所有该类自己生命的方法，不问访问权限
     */
    Method[] ms = c.getMethods();
    c.getDeclaredMethods();
    for (int i = 0;i<ms.length;i++) {
      //得到方法的返回值类型
      Class returnType = ms[i].getReturnType();
      System.out.print(returnType.getName()+" ");
      //得到方法的名称
      System.out.print(ms[i].getName());
      System.out.print("(");
      //获取参数类型--->得到的是参数列表的类型的  类类型。譬如参数是int。得到的就是int.class
      Class[] paramTypes = ms[i].getParameterTypes();
      for (Class class1 : paramTypes) {
        System.out.print(class1.getName()+",");
      }
      System.out.println(")");
      printFieldMessage(c);

    }
  }

  public static void printFieldMessage(Object object) {
    Class<?> c = object.getClass();
    /*
     * 成员变量也是对象
     * java.lang.reflect.Field的对象
     * Field类封装了关于成员变量的操作
     * getFields()方法获取的是所有的public的成员变量的信息
     * getDeclaredFields获取的是该类自己声明的成员变量的信息
     */
//      Field[] fs = c.getFields();
    Field[] fs = c.getDeclaredFields();
    for (Field field : fs) {
      //得到成员变量类型的类类型
      Class<?> fieldType = field.getType();
      String typeName = fieldType.getName();
      //得到成员变量的名称
      System.out.println("typeName:"+typeName);
    }
  }

  /**
   * 打印对象构造函数的信息
   * @param object
   */
  public static void printConMessage(Object object) {
    Class<?> c = object.getClass();
    /*
    * 构造函数也是对象
    * java.lang.Constructor中封装了构造函数的信息
    * getConstructors获取所有的public的构造函数
    * getDeclaredConstructors得到所有的的构造函数
    */
    Constructor<?>[] cs;
    cs = c.getConstructors();
    for (Constructor<?> constructor : cs) {
      //获取构造函数的参数列表---得到的是构造函数参数的类类型
      Class<?>[] paramTypes = constructor.getParameterTypes();
      System.out.print("(");
      for (Class<?> class1 : paramTypes) {
        System.out.print(class1.getName()+",");
      }
      System.out.println(")");
    }
  }

}
