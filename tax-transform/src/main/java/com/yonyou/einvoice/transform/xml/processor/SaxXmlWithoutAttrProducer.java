package com.yonyou.einvoice.transform.xml.processor;

import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.xml.json.Sax2JSONWithoutAttrHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX的会计科目信息提取producer
 *
 * @author liuqiangm
 */
public class SaxXmlWithoutAttrProducer extends DefaultHandler {

  Sax2JSONWithoutAttrHandler jsonWithoutAttrHandler = new Sax2JSONWithoutAttrHandler();
  private final StringBuilder stringBuilder = new StringBuilder();

  private Set<String> firstIterPathSet = new HashSet<>();

  private Map<String, AbstractXmlConsumer> xmlConsumerMap = new LinkedHashMap<>();

  List<AbstractXmlConsumer> curProcessConsumers = new ArrayList<>();

  /**
   * 判断是否当前节点时consumer需要消费的节点或子节点
   */
  boolean flag = false;

  // 遍历树时的模式，用于模式匹配
  private LinkedList<String> pathList = new LinkedList<>();

  /**
   * 生命周期管理。开始消费
   *
   * @throws SAXException
   */
  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
  }

  /**
   * 生命周期管理。结束消费
   *
   * @throws SAXException
   */
  @Override
  public void endDocument() throws SAXException {
    curProcessConsumers.forEach(xmlConsumer -> xmlConsumer.end());
    super.endDocument();
  }

  /**
   * 解析xml元素
   */
  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    pathList.addLast(qName);
    String pathStr = this.getPathStr();
    boolean pathContains = xmlConsumerMap.containsKey(pathStr);
    if (pathContains) {
      flag = true;
    }
    if (flag) {
      jsonWithoutAttrHandler.startElement(uri, localName, qName, attributes);
    }
    super.startElement(uri, localName, qName, attributes);
  }

  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    // 当前节点为需要消费的节点或子节点
    if (flag) {
      jsonWithoutAttrHandler.endElement(uri, localName, qName);
      super.endElement(uri, localName, qName);
    }
    String pathStr = getPathStr();
    // 当前节点为需要消费的节点（非子节点）
    if (xmlConsumerMap.containsKey(pathStr)) {
      // 本次消费的节点已经拼接完毕，需要将flag设置为false。
      flag = false;
      JSONObject root = jsonWithoutAttrHandler.getRoot();
      Object obj = root.get(pathList.peekLast());
      AbstractXmlConsumer consumer = xmlConsumerMap.get(pathStr);
      // 如果是第一次遍历，则调用start()方法
      if (!firstIterPathSet.contains(pathStr)) {
        consumer.start();
        firstIterPathSet.add(pathStr);
        curProcessConsumers.add(consumer);
      }
      // 进行消费
      consumer.consume(obj);
      // 设置本次拼接得到的jsonObject为空。
      jsonWithoutAttrHandler.setRoot(null);
    }
    pathList.removeLast();
    super.endElement(uri, localName, qName);
  }

  @Override
  public void characters(char[] ch, int start, int length)
      throws SAXException {
    if (flag) {
      jsonWithoutAttrHandler.characters(ch, start, length);
    }
  }

  /**
   * 获取当前节点的路径。
   *
   * @return
   */
  private String getPathStr() {
    // 复用stringBuilder，减少gc开销
    stringBuilder.setLength(0);
    if (pathList.size() == 0) {
      return "";
    }
    Iterator<String> iterator = pathList.iterator();
    stringBuilder.append(iterator.next());
    while (iterator.hasNext()) {
      stringBuilder.append(".").append(iterator.next());
    }
    return stringBuilder.toString();
  }

  /**
   * 添加节点的消费者
   *
   * @param xmlConsumer
   */
  public void addConsumer(AbstractXmlConsumer xmlConsumer) {
    String xmlPath = xmlConsumer.getXmlPath();
    xmlConsumerMap.keySet().forEach(key -> {
      if (xmlPath.startsWith(key) || key.startsWith(xmlPath)) {
        throw new RuntimeException("xml的不同消费者节点，标签路径不能有上下级关系，请检查：" + key + "," + xmlPath);
      }
    });
    xmlConsumerMap.put(xmlConsumer.getXmlPath(), xmlConsumer);
  }

}
