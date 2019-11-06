package com.geely.design.pattern.structural.facade;

/**
 * 2019/11/6 下午9:04
 *
 * @author liangbingtian
 */
public class QualifyService {
  public boolean isAvailable(PointsGirt pointsGirt) {
    System.out.println("校验"+pointsGirt.getName()+" 积分资格通过,库存通过");
    return true;
  }

}
