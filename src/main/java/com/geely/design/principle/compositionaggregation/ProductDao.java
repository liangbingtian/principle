package com.geely.design.principle.compositionaggregation;

/**
 * liangbingtian 2019/5/20 上午12:18
 */
public class ProductDao{

  private DBConnection dbConnection;

  public void setDbConnection(DBConnection dbConnection) {
    this.dbConnection = dbConnection;
  }
}
