package com.geely.design.pattern.creational.singleton;

/**
 * 2019/10/28 下午7:57
 *
 * @author liangbingtian
 *
 */
public class ThreadLocalInstance {
  // 匿名类,重写方法
  private static final ThreadLocal<ThreadLocalInstance> threadLocalInstanceThreadLocal =
      new ThreadLocal<ThreadLocalInstance>() {
        @Override
        protected ThreadLocalInstance initialValue() {
          return new ThreadLocalInstance();
        }
      };

  private ThreadLocalInstance () {

  }

  public static ThreadLocalInstance getInstance() {
    return threadLocalInstanceThreadLocal.get();
  }



}
