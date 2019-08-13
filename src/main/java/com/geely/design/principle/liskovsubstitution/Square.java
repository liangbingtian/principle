package com.geely.design.principle.liskovsubstitution;

/**
 * liangbingtian 2019/5/19 下午11:03
 */
public class Square implements Quadrangle {

  private long sideLength;

  public long getSideLength() {
    return sideLength;
  }

  public void setSideLength(long sideLength) {
    this.sideLength = sideLength;
  }

  public long getWidth() {
    return sideLength;
  }

  public long getLength() {
    return sideLength;
  }
}
