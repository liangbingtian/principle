package com.geely.design.pattern.creational.simplefactory;

/**
 * liangbingtian 2019/5/21 上午12:44
 */
public class VideoFactory {

//  public Vedio getVedio(String type) {
//    if ("java".equalsIgnoreCase(type)){
//      return new JavaVideo();
//    }else if ("python".equalsIgnoreCase(type)){
//      return new PythonVedio();
//    }
//    return null;
//  }

  public Vedio getVedio(Class c){
    Vedio vedio = null;
    try {
      vedio = (Vedio) c.forName(c.getName()).newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return vedio;
  }

}
