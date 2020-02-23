package reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.junit.Test;
import utils.ClassUtil;

/**
 * Created by liangbingtian on 2020/2/22 6:37 下午
 */
public class ClassDemoTest {

  @Test
  public void classDemo1()
      throws ClassNotFoundException, IllegalAccessException, InstantiationException {
    //Foo的实例对象如何表示
    Foo foo1 = new Foo();
    //Foo 这个类也是个实例对象,Class类的实例对象，如何表示
    //任何一个类都是Class的实例对象，这个实例对象有三种表示方式

    //第一种-->实际在告诉我们任何一个类，都有一个隐含的静态成员变量class
    Class c1 = Foo.class;
    //第二种表达方式，已知该类的对象。通过getClass方法
    Class c2 = foo1.getClass();

    /*官网说:c1,c2表示了Foo类的类类型(class type)
    * 万事万物皆对象，
    * 类也是对象，是class类的实例对象
    * 这个对象我们成为该类的类类型
    * */
    //不管c1 or c2都代表了Foo类的类类型，一个类只可能是Class类的一个实例对象
    System.out.println(c1 == c2);

    //第三种表达方式
    Class c3 = null;
    c3 = Class.forName("reflect.ClassDemoTest.Foo");
    System.out.println(c2 == c3);

    //我们完全可以通过类的类类型创建该类的对象实例--->通过c1 or c2 or c3创建Foo的实例
    Foo foo = (Foo) c1.newInstance();
  }

  @Test
  public void classDemo2(){
    Class c1 = int.class;//int 的类类型
    Class c2 = String.class;//String类的类类型
    Class c3 = double.class;
    Class c4 = Double.class;
    Class c5 = void.class;

    System.out.println(c1.getName());
    System.out.println(c1.getName());
    System.out.println(c2.getSimpleName());
    System.out.println(c5.getName());
  }

  @Test
  public void classDemo3(){
    String s = "hello";
    ClassUtil.printClassMessage(s);
    System.out.println("==============");
  }

  @Test
  public void classDemo4()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    //要获取print(int, int)方法 1.要获取一个方法就是获取类的信息，获取类的信息首先要获取类的类类型
    A a1 = new A();
    Class<?> c = a1.getClass();
    /*
     * 2.获取方法 名称和参数列表来决定
     * getMethod获取的是public方法
     * getDeclaredMethod获得自己声明的方法
     */
    Method m = c.getMethod("print", int.class, int.class);
    //方法的反射操作是用m对象来进行方法调用 和a1.print调用的效果完全相同
//    a1.print(10, 20);
    //方法如果没有返回值，返回null,有返回值返回具体的返回值。
    Object o = m.invoke(a1, 10, 20);
    System.out.println("===============");
    //获取方法对象,对方法进行反射操作
    Method m1 = c.getMethod("print", String.class, String.class);
    Object hello = m1.invoke(a1, "hello", "hello");
  }

  @Test
  public void classDemo5()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    ArrayList list = new ArrayList();

    ArrayList<String> list1 = new ArrayList<>();
    list1.add("hello");
//    list1.add(20); 是错误的
    Class<?> c1 = list.getClass();
    Class<?> c2 = list1.getClass();
    System.out.println(c1 == c2);

    /**
     * c1 == c2结果返回true说明编译之后的集合的泛型是去泛型化的
     * Java中集合的泛型，是防止错误输入的，只在编译阶段有效
     * 绕过编译就无效了
     * 验证：我们可以通过方法的反射来操作，绕过编译
     */
    Method m = c1.getMethod("add", Object.class);
    m.invoke(list1, 1);//绕过编译操作就绕过了泛型
    System.out.println(list1.size());
//    for (String string : list1) {
//      System.out.println(string);
//    }
// 现在无法这样遍历了。遍历会报类型转换错误

  }

  class Foo {

  }

  class A{
    public void print(int a, int b){
      System.out.println(a + b);
    }
    public void print(String a, String b){
      System.out.println(a.toUpperCase()+","+b.toLowerCase());
    }
  }

}
