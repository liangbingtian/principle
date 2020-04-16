package com.mall.concurrency.example.atomic;

import com.mall.concurrency.annoations.ThreadSafe;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by liangbingtian on 2020/4/14 8:26 下午
 */
@Slf4j
@ThreadSafe
public class AtomicExample5 {

  //修饰的fieldName字段必须是volatile并且是非static修饰的字段。
  private static AtomicIntegerFieldUpdater<AtomicExample5> updater = AtomicIntegerFieldUpdater.newUpdater(AtomicExample5.class, "count");

  @Getter
  public volatile int count = 100;

  public static void main(String[] args) {
    AtomicExample5 example5 = new AtomicExample5();
    if (updater.compareAndSet(example5, 100, 120)) {
      log.info("update success1, {}", example5.getCount());
    }

    if (updater.compareAndSet(example5, 100, 120)) {
      log.info("update success2, {}", example5.getCount());
    } else {
      log.info("update failed, {}", example5.getCount());
    }
  }
}
