package com.geely.design.principle.compositionaggregation;

/**
 * liangbingtian 2019/5/20 上午12:22
 */
public class MySqlConnection extends DBConnection{

  public String getConnection() {
    return "mysql数据库连接";
  }
}
