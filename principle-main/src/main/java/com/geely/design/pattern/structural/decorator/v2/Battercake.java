package com.geely.design.pattern.structural.decorator.v2;


/**
 * 2019/11/5 上午12:32
 *
 * @author liangbingtian
 */
public class Battercake extends ABattercake {

  @Override
  protected String getDesc(){
    return "煎饼";
  }

  @Override
  protected int cost(){
    return 8;
  }
}
