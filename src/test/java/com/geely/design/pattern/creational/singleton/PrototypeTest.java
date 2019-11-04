package com.geely.design.pattern.creational.singleton;

import com.geely.design.pattern.creational.prototype.Mail;
import com.geely.design.pattern.creational.prototype.MailUtil;
import com.geely.design.pattern.creational.prototype.clone.Pig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import org.junit.Test;

/**
 * 2019/10/28 下午8:37
 *
 * @author liangbingtian
 */
public class PrototypeTest {

  /**
   * 原型模式测试。原型模式是在内存中进行一个二进制流的拷贝。比直接new性能好很多
   */
  @Test
  public void test1() throws CloneNotSupportedException {
    Mail mail = new Mail();
    mail.setContent("初始化模板");

    for (int i = 0;i<10;i++) {
      Mail mailTemp = (Mail) mail.clone();
      mailTemp.setName("姓名"+i);
      mailTemp.setEmailAddress("姓名"+i+"@imooc.com");
      mailTemp.setContent("恭喜您活动中奖了");
      MailUtil.sendMail(mail);
    }

    MailUtil.saveOriginMailRecord(mail);
  }

  /**
   * 原型模式测试。用于测试深拷贝和浅拷贝
   */
  @Test
  public void test2()
      throws CloneNotSupportedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Date birthday = new Date(0L);
    Pig pig1 = new Pig("佩奇", birthday);
    Method method = pig1.getClass().getDeclaredMethod("clone");
    method.setAccessible(true);
    Pig pig2 = (Pig) method.invoke(pig1);
    pig1.getBirthday().setTime(66666666L);
    System.out.println(pig1);
    System.out.println(pig2);
  }

  /**
   * 克隆破坏单例
   */
  @Test
  public void test3()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    HungrySingleton hungrySingleton = HungrySingleton.getInstance();
    //当方法是protect的时候直接用反射调用就可以了
    Method method = hungrySingleton.getClass().getDeclaredMethod("clone");
    method.setAccessible(true);
    HungrySingleton cloneHungrySingleton = (HungrySingleton) method.invoke(hungrySingleton);
    System.out.println(hungrySingleton);
    System.out.println(cloneHungrySingleton);
    //预防克隆破坏,单例不实现cloneable接口或者实现接口后重写clone方法。
  }

}
