package com.yonyou.einvoice.transform.xml.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.xml.json.Sax2JSONWithAttrHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.InputSource;

@Slf4j
public class XmlProcessUtils {

  /**
   * 将输入源的xml数据根据xmlConsumer进行处理
   *
   * @param xmlInputStream
   * @param xmlConsumerList
   */
  public static void processWithoutAttr(InputStream xmlInputStream,
      List<AbstractXmlConsumer> xmlConsumerList) {
    processWithoutAttr(xmlInputStream, xmlConsumerList, "UTF-8");
  }

  /**
   * 将输入源的xml数据根据xmlConsumer进行处理
   *
   * @param xmlFile
   * @param xmlConsumerList
   */
  public static void processWithoutAttr(File xmlFile, List<AbstractXmlConsumer> xmlConsumerList) {
    try {
      processWithoutAttr(Files.newInputStream(xmlFile.toPath()), xmlConsumerList);
    } catch (IOException e) {
      log.error("", e);
    }
  }

  /**
   * 将输入源的xml数据根据xmlConsumer进行处理
   *
   * @param xmlFile
   * @param xmlConsumerList
   */
  public static void processWithAttr(File xmlFile, List<? extends AbstractXmlConsumer> xmlConsumerList) {
    try {
      processWithAttr(Files.newInputStream(xmlFile.toPath()), xmlConsumerList);
    } catch (IOException e) {
      log.error("", e);
    }
  }

  /**
   * 将输入源的xml数据根据xmlConsumer进行处理
   *
   * @param xmlInputStream
   * @param xmlConsumerList
   */
  public static void processWithAttr(InputStream xmlInputStream,
      List<? extends AbstractXmlConsumer> xmlConsumerList) {
    processWithAttr(xmlInputStream, xmlConsumerList, "UTF-8");
  }

  /**
   * 将输入源的xml数据根据xmlConsumer进行处理。指定编码格式
   *
   * @param xmlInputStream
   * @param xmlConsumerList
   * @param encoding
   */
  public static void processWithoutAttr(InputStream xmlInputStream,
      List<? extends AbstractXmlConsumer> xmlConsumerList, String encoding) {
    InputSource inputSource = new InputSource(xmlInputStream);
    inputSource.setEncoding(encoding);
    SaxXmlWithoutAttrProducer producer = new SaxXmlWithoutAttrProducer();
    xmlConsumerList.forEach(iXmlConsumer -> producer.addConsumer(iXmlConsumer));
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    try {
      SAXParser saxParser = parserFactory.newSAXParser();
      saxParser.parse(inputSource, producer);
      xmlInputStream.close();
    } catch (Exception e) {
      log.error("", e);
    }
  }

  /**
   * 将输入源的xml数据根据xmlConsumer进行处理。指定编码格式
   *
   * @param xmlInputStream
   * @param xmlConsumerList
   * @param encoding
   */
  public static void processWithAttr(InputStream xmlInputStream,
      List<? extends AbstractXmlConsumer> xmlConsumerList, String encoding) {
    InputSource inputSource = new InputSource(xmlInputStream);
    inputSource.setEncoding(encoding);
    SaxXmlWithAttrProducer producer = new SaxXmlWithAttrProducer();
    xmlConsumerList.forEach(iXmlConsumer -> producer.addConsumer(iXmlConsumer));
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    try {
      SAXParser saxParser = parserFactory.newSAXParser();
      saxParser.parse(inputSource, producer);
      xmlInputStream.close();
    } catch (Exception e) {
      log.error("", e);
    }
  }

  /**
   * 获取指定标签内的属性值
   *
   * @param tagJSONObject
   * @param attr
   * @return
   */
  public static String getAttrValue(JSONObject tagJSONObject, String attr) {
    JSONObject attrJSONObject = tagJSONObject.getJSONObject(Sax2JSONWithAttrHandler.ATTR);
    return attrJSONObject.getString(attr);
  }

  /**
   * 获取指定标签下的子标签json列表
   *
   * @param tagJSONObject
   * @param subTag
   * @return
   */
  public static List<JSONObject> getSubTagList(JSONObject tagJSONObject, String subTag) {
    JSONArray jsonArray = tagJSONObject.getJSONArray(Sax2JSONWithAttrHandler.ARR);
    if (jsonArray == null) {
      return Collections.emptyList();
    }
    List<JSONObject> jsonObjects = new ArrayList<>(jsonArray.size());
    for (int i = 0; i < jsonArray.size(); i++) {
      Object obj = jsonArray.get(i);
      if (obj instanceof JSONObject && ((JSONObject) obj).containsKey(subTag)) {
        jsonObjects.add(((JSONObject) obj).getJSONObject(subTag));
      }
    }
    return jsonObjects;
  }

  /**
   * 获取指定标签下的value常量值
   *
   * @param tagJSONObject
   * @return
   */
  public static String getTagValue(JSONObject tagJSONObject) {
    return tagJSONObject.getString(Sax2JSONWithAttrHandler.VALUE);
  }

}
