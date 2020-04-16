package com.yonyou.einvoice.transform.xml.rebuild;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * xml重建器。用于对指定xml标签下的子标签添加索引列表
 * @author liuqiangm
 */
public class XMLRebuilder extends DefaultHandler {

  private boolean rebuild = false;

  private XMLTreeNode root;

  private String value;

  private LinkedList<XMLTreeNode> treeNodes = new LinkedList<>();

  private List<AbstractRebuildStrategy> rebuildStrategyList = new ArrayList<>();

  /**
   * 解析xml元素
   */
  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    treeNodes.push(new XMLTreeNode(qName));
    super.startElement(uri, localName, qName, attributes);
  }

  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    super.endElement(uri, localName, qName);
    XMLTreeNode currNode = treeNodes.pop();
    if (treeNodes.size() == 0) {
      root = currNode;
    } else {
      treeNodes.peek().addSunNode(currNode);
    }
    currNode.setValue(value);
    value = null;
  }

  @Override
  public void characters(char[] ch, int start, int length)
      throws SAXException {
    super.characters(ch, start, length);
    value = new String(ch, start, length).trim();
  }

  public String getRebuildedStr() {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    writeToOutputStream(byteArrayOutputStream);
    try {
      return byteArrayOutputStream.toString("UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return "";
    }
  }

  public void writeToOutputStream(OutputStream outputStream) {
    if (!rebuild) {
      for(AbstractRebuildStrategy rebuildStrategy : rebuildStrategyList) {
        rebuildStrategy.treeRebuild(root, "");
        rebuildStrategy.setRebuild(true);
      }
      rebuild = true;
    }
    XMLOutputUtils.writeXMLToOutputStream(root, outputStream);
  }

  public void addRebuildStrategy(AbstractRebuildStrategy rebuildStrategy) {
    this.rebuildStrategyList.add(rebuildStrategy);
  }

  public void addAllRebuildStrategy(List<AbstractRebuildStrategy> rebuildStrategy) {
    this.rebuildStrategyList.addAll(rebuildStrategy);
  }
}
