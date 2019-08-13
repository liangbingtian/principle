package com.geely.design.pattern.creational.singleton;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 2019/8/13 下午11:31
 *
 * @author liangbingtian
 */
public class LazySingletonTest {

  /**
   * 懒汉普通模式，单线程直接获取单例对象
   */
  @Test
  public void test1() {
    LazySingleton lazySingleton = LazySingleton.getInstance();
    System.out.println("program end");
  }

  public void test2() {
    
  }
}
