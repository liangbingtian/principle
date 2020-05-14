package com.mall.concurrency.publish;

import com.mall.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by liangbingtian on 2020/4/18 9:10 下午
 */
@Slf4j
@NotThreadSafe
public class Escape {

  private int thisCanBeEscape = 0;

  public Escape() {
    new InnerClass();
  }

  private class InnerClass {
    public InnerClass() {
      log.info("{}", Escape.this.thisCanBeEscape);
    }
  }

  public static void main(String[] args) {
    new Escape();
  }

}
