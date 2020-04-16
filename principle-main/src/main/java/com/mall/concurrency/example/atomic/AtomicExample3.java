package com.mall.concurrency.example.atomic;

import com.mall.concurrency.annoations.ThreadSafe;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by liangbingtian on 2020/4/12 10:19 下午
 */
@Slf4j
@ThreadSafe
public class AtomicExample3 {

  //请求总数
  public static int clientTotal = 5000;

  //同时并发执行的线程数
  public static int threadTotal = 200;

  public static LongAdder count = new LongAdder();

  public static void main(String[] args) throws Exception{
    ExecutorService executorService = Executors.newCachedThreadPool();
    final Semaphore semaphore = new Semaphore(threadTotal);
    final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
    for (int i = 0; i<clientTotal; i++) {
      executorService.execute(()->{
        try {
          //acquire时候是判断当前进程是否允许被执行
          semaphore.acquire();
          //如果达到了一定的并发数,add()方法会被临时阻塞掉
          add();
          //当acquire返回一定值的时候当前进程允许被执行。进程执行完之后要用release释放掉进程。
          semaphore.release();
        }catch (Exception e) {
          log.error("exception", e);
        }
        //每执行完一个，其对应的计数值就会减一
        countDownLatch.countDown();
      });
    }
    countDownLatch.await();
    //如果我们希望在所有线程都执行完后打印当前计数的值
    //关闭线程池
    executorService.shutdown();
    log.info("count:{}", count);
  }

  //对于共享变量的这种操作是线程不安全的写法
  private static void add() {
    count.increment();
    //如果增加的值不是1的话那么调用add的方法就行
  }

}
