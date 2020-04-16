package com.geely.design.principle.compositionaggregation;

/**
 * liangbingtian 2019/5/20 上午12:19
 */
public class Test {

  public static void main(String[] args) {
    ProductDao productDao = new ProductDao();
    productDao.setDbConnection(new MySqlConnection());
  }

}
