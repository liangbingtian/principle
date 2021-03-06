package com.mall.concurrency.singleton;

import com.mall.concurrency.annoations.ThreadSafe;

/**
 * Created by liangbingtian on 2020/4/18 9:19 下午
 * 懒汉模式：单例的实例在第一次使用的时候进行创建 -->双重同步锁单例模式
 * volatile+双重检测机制,限制刚才的那段代码进行指令重排
 */
@ThreadSafe
public class SingletonExample5 {

  /**
   * 如何保证一个类只能初始化一次，那么就要保证这个类不能随便new一个新的对象出来。
   * 那么就要设置该类的私有构造方法，只有构造方法私有了
   * ，外边才不能一直new这个对象出来
   */
  private SingletonExample5() {

  }

  //单例对象
  private volatile static SingletonExample5 instance = null;

  //通过静态工厂方法来获取单例对象
  //在方法上加synchronized保证了线程安全，但是带来了性能上的开销
  public static SingletonExample5 getInstance() {
    if (instance == null) {//双重检测机制
      synchronized (SingletonExample5.class) {//同步锁
        if (instance == null) {
          instance = new SingletonExample5();
        }
      }
    }
    return instance;
  }

}
