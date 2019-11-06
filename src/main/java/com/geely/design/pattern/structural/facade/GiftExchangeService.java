package com.geely.design.pattern.structural.facade;

/**
 * 2019/11/6 下午9:13
 *
 * @author liangbingtian
 */
public class GiftExchangeService {
  private QualifyService qualifyService;
  private PointsPaymentService pointsPaymentService;
  private ShippingService shippingService;

  public void setQualifyService(QualifyService qualifyService) {
    this.qualifyService = qualifyService;
  }

  public void setPointsPaymentService(
      PointsPaymentService pointsPaymentService) {
    this.pointsPaymentService = pointsPaymentService;
  }

  public void setShippingService(
      ShippingService shippingService) {
    this.shippingService = shippingService;
  }

  public void giftExchange(PointsGirt pointsGirt) {
    if (qualifyService.isAvailable(pointsGirt)) {
      //资格校验通过
      if (pointsPaymentService.pay(pointsGirt)) {
        String shippingOrderNo = shippingService.shipGift(pointsGirt);
        System.out.println("物流系统下单成功,订单号是:"+shippingOrderNo);
      }
    }
  }
}
