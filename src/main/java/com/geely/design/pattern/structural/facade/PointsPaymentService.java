package com.geely.design.pattern.structural.facade;

/**
 * 2019/11/6 下午9:06
 *
 * @author liangbingtian
 */
public class PointsPaymentService {
  public boolean pay(PointsGirt pointsGirt){
    //扣减积分
    System.out.println("支付"+pointsGirt.getName()+" 积分成功");
    return true;
  }

}
