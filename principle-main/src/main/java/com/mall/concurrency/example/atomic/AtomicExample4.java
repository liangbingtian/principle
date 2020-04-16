package com.mall.concurrency.example.atomic;

import com.mall.concurrency.annoations.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by liangbingtian on 2020/4/14 8:26 下午
 */
@Slf4j
@ThreadSafe
public class AtomicExample4 {

  private static AtomicReference<Integer> count = new AtomicReference<>(0);

  public static void main(String[] args) {
    count.compareAndSet(0, 1);
  }
}
