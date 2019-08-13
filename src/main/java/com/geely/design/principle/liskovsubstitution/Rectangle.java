package com.geely.design.principle.liskovsubstitution;

/**
 * liangbingtian 2019/5/19 下午11:02
 */
public class Rectangle implements Quadrangle{

  private long length;

  private long width;


  public long getWidth() {
    return width;
  }

  public long getLength() {
    return length;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public void setWidth(long width) {
    this.width = width;
  }
}
