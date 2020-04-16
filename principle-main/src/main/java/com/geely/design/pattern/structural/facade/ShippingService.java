package com.geely.design.pattern.structural.facade;

/**
 * 2019/11/6 下午9:07
 *
 * @author liangbingtian
 */
public class ShippingService {
  public String shipGift(PointsGirt pointsGirt) {
    //物流系统的对接逻辑
    System.out.println(pointsGirt.getName()+"进入物流系统");
    String shippingOrderNo = "666";
    return shippingOrderNo;
  }

}
