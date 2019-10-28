package com.geely.design.pattern.creational.singleton;

/**
 * 2019/9/17 下午4:51
 *
 * @author liangbingtian
 * 小作业，通过反编译工具jad去查看printTest
 */
public enum EnumInstance {

  INSTANCE{
    @Override
    protected void printTest() {
      System.out.println("Geely print test");
    }
  };

  protected abstract void printTest();

  private Object data;

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public static EnumInstance getInstance() {
    return INSTANCE;
  }
}
