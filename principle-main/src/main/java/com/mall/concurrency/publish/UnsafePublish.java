package com.mall.concurrency.publish;

import com.mall.concurrency.annoations.NotThreadSafe;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by liangbingtian on 2020/4/18 8:55 下午
 */
@Slf4j
@NotThreadSafe
public class UnsafePublish {

  public String[] states = {"a", "b", "c"};

  public String[] getStates() {
    return states;
  }

  public static void main(String[] args) {
    UnsafePublish unsafePublish = new UnsafePublish();
    log.info("{}", Arrays.toString(unsafePublish.getStates()));
    unsafePublish.getStates()[0] = "d";
    log.info("{}", Arrays.toString(unsafePublish.getStates()));
  }

}
