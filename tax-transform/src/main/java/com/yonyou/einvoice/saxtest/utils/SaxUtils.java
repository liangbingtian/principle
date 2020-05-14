package com.yonyou.einvoice.saxtest.utils;

import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by liangbingtian on 2020/5/7 7:53 下午
 */
@Slf4j
public class SaxUtils {

  public static Document read(File file) throws DocumentException {
    SAXReader reader = new SAXReader();
    Document document  = reader.read(file);
    Element element = document.getRootElement();
    String xpath = element.getPath();
    String value = element.getTextTrim();
    Element parent = element.getParent();
    String name = element.getName();
    List<Element> elementList = element.elements();
    Element element1 = elementList.get(0);
    List<Element> elementList1 = element1.elements();
//    for (Element elementSub : elementList1) {
//      String xpath1 = elementSub.getPath();
//      String name1 = elementSub.getName();
//      Element element2 = element1.element("sjdkseybxmbnlj");
//      System.out.println("");
//    }
    Element element2 = element1.element("zzsnssbbsyyzzsybnsrGridlbVO");
    element2.elements();
    return document;
  }
}
