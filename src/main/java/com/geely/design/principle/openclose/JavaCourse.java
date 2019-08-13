package com.geely.design.principle.openclose;

/**
 * liangbingtian 2019/5/18 下午10:43
 */
public class JavaCourse implements ICourse {

  private Integer Id;
  private String name;
  private Double price;

  public JavaCourse(Integer id, String name, Double price) {
    Id = id;
    this.name = name;
    this.price = price;
  }

  public Integer getId() {
    return this.Id;
  }

  public String getName() {
    return this.name;
  }

  public Double getPrice() {
    return this.price;
  }
}
