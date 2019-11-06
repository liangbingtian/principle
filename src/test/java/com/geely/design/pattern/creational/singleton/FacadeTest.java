package com.geely.design.pattern.creational.singleton;

import com.geely.design.pattern.structural.facade.GiftExchangeService;
import com.geely.design.pattern.structural.facade.PointsGirt;
import com.geely.design.pattern.structural.facade.PointsPaymentService;
import com.geely.design.pattern.structural.facade.QualifyService;
import com.geely.design.pattern.structural.facade.ShippingService;
import org.junit.Test;

/**
 * 2019/11/6 下午9:32
 *
 * @author liangbingtian
 */
public class FacadeTest {

  @Test
  public void test1() {
    PointsGirt pointsGirt = new PointsGirt("T恤");
    GiftExchangeService giftExchangeService = new GiftExchangeService();
    giftExchangeService.setQualifyService(new QualifyService());
    giftExchangeService.setPointsPaymentService(new PointsPaymentService());
    giftExchangeService.setShippingService(new ShippingService());

    giftExchangeService.giftExchange(pointsGirt);

  }

}
