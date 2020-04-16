package com.mall.concurrency.example.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by liangbingtian on 2020/4/14 10:34 下午
 */
@Slf4j
public class SynchronizedExample2 {

  //修饰一个类
  public static void test1(int j) {
    synchronized (SynchronizedExample2.class) {
      for (int i = 0 ;i<10;i++) {
        log.info("test{} - {}", j, i);
      }
    }
  }

  //修饰一个静态方法
  public static synchronized void test2() {
    for (int i = 0 ;i<10;i++) {
      log.info("test2 - {}", i);
    }
  }

  public static void main(String[] args) {
    SynchronizedExample2 synchronizedExample1 = new SynchronizedExample2();
    SynchronizedExample2 synchronizedExample2 = new SynchronizedExample2();
    ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * 开启两个进程去执行这个方法。为什么使用线程池，如果不使用线程池，两次调用同一个方法，
     * 那么他们本身就是同步执行的。因此我们是没办法验证他们的具体影响的。而我们加上了线程池之后
     * ，它其实是分别启动了两个进程去执行，它在第一个方法执行时，不等第一个方法执行完，就去执行下一个方法。
     * 我们才能看到一个对象的两个进程调用这段代码时候的执行情况。
     */
    executorService.execute(()-> test1(1));
    executorService.execute(()-> test1(2));
  }

}
