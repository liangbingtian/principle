package com.geely.design.pattern.creational.singleton;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 2019/8/13 下午11:31
 *
 * @author liangbingtian
 */
public class SingletonTest {

  /**
   * 懒汉普通模式，单线程直接获取单例对象
   */
  @Test
  public void test1() {
    LazySingleton lazySingleton = LazySingleton.getInstance();
    System.out.println("program end");
  }

  /**
   * 懒汉普通模式，多线程获取单例对象的时候所出的问题
   */
  @Test
  public void test2() {
    Thread thread1 = new Thread(new T());
    Thread thread2 = new Thread(new T());
    thread1.start();
    thread2.start();
    System.out.println("program end");
  }

  /**
   * 容器单例模式，运用放置进map的时候key相同的原理。放置进去的时候可以放置进去多个，但是取的时候只会取一个。
   */
  @Test
  public void test3() {
    Thread thread1 = new Thread(new ContainerT());
    Thread thread2 = new Thread(new ContainerT());
    thread1.start();
    thread2.start();
    System.out.println("Program end");
  }

  private class ContainerT implements Runnable {

    public void run() {
      ContainerSingleton.putInstance("object", new Object());
      Object instance = ContainerSingleton.getInstance("object");
      System.out.println(Thread.currentThread().getName() + " " + instance);
    }
  }
}
