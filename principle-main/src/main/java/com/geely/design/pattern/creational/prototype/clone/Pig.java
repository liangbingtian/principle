package com.geely.design.pattern.creational.prototype.clone;

import java.util.Date;

/**
 * 2019/11/4 下午9:11
 *
 * @author liangbingtian
 */
public class Pig implements Cloneable{

  private String name;
  private Date birthday;

  public Pig(String name, Date birthday) {
    this.name = name;
    this.birthday = birthday;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    //深克隆
    Pig pig = (Pig) super.clone();
    pig.birthday = (Date) pig.birthday.clone();
    return pig;
  }

  @Override
  public String toString() {
    return "Pig{" +
        "name='" + name + '\'' +
        ", birthday=" + birthday +
        '}';
  }
}
