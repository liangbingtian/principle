package com.geely.design.pattern.structural.decorator.v1;

import com.geely.design.pattern.structural.decorator.common.IBattercake;

/**
 * 2019/11/5 上午12:19
 *
 * @author liangbingtian
 */
public class Battercake implements IBattercake {
  protected String getDesc(){
    return "煎饼";
  }
  protected int cost(){
    return 8;
  }

}
