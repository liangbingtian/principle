package com.geely.design.pattern.structural.decorator.v1;

/**
 * 2019/11/5 上午12:20
 *
 * @author liangbingtian
 */
public class BattercakeWithEgg extends Battercake{

  @Override
  public String getDesc() {
    return super.getDesc()+" 加一个鸡蛋";
  }

  @Override
  public int cost() {
    return super.cost()+1;
  }
}
