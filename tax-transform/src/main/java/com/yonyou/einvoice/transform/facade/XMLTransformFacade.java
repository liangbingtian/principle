package com.yonyou.einvoice.transform.facade;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.xml.json.Sax2JSONWithAttrHandler;
import com.yonyou.einvoice.transform.xml.json.Sax2JSONWithoutAttrHandler;
import com.yonyou.einvoice.transform.xml.rebuild.AbstractRebuildStrategy;
import com.yonyou.einvoice.transform.xml.rebuild.XMLRebuilder;
import com.yonyou.einvoice.transform.xml.rebuild.rowtocol.XMLRowToColRebuildStrategy;
import com.yonyou.einvoice.transform.xml.rebuild.subindex.XMLAddIndexRebuildStrategy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * xml转换器facade类。 支持如下三种转换 1. xml固定路径下行列转换 2. xml转json 3. xml转json，指定路径转为json数组
 *
 * @author liuqiangm
 */
public class XMLTransformFacade {

  // 默认字符集
  private static final String CHARSET = "UTF-8";

  private static final String ELEM = "elem";

  /**
   * 获取xml行列转换后的字符串
   *
   * @param xmlStr xml字符串
   * @param colToRowPath 需要行列转换的xml路径
   */
  public static String getColToRowXmlStr(String xmlStr, String colToRowPath) {
    ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    return getColToRowXmlStr(xmlInputStream, colToRowPath);
  }

  /**
   * 获取xml行列转换后的字符串
   *
   * @param xmlStr xml字符串
   * @param colToRowPath 需要行列转换的xml路径
   * @param specificInputStream 指定值json的inputStream
   */
  public static String getColToRowXmlStrWithSpecificInputStream(String xmlStr, String colToRowPath,
      InputStream specificInputStream) {
    ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    return getColToRowXmlStrWithSpecificInputStream(xmlInputStream, colToRowPath,
        specificInputStream);
  }

  /**
   * 获取xml行列转换后的字符串。如果map的key包含xml标签，则key为map的value（同样为map），值插入到该xml标签下
   *
   * @param xmlStr xml字符串
   * @param colToRowPath 需要行列转换的xml路径
   * @param specificMap 指定值map
   */
  public static String getColToRowXmlStrWithSpecificMap(String xmlStr, String colToRowPath,
      Map<String, Map<String, String>> specificMap) {
    ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    return getColToRowXmlStrWithSpecificMap(xmlInputStream, colToRowPath, specificMap);
  }

  public static String getXmlStrWithTransformStrategy(String xmlStr, List<AbstractRebuildStrategy> strategies) {
    ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    return getXmlStrWithTransformStrategy(xmlInputStream, strategies);
  }

  /**
   * 获取xml行列转换后的字符串
   *
   * @param xmlInputStream xml输入流
   * @param colToRowPath 需要行列转换的xml路径
   */
  public static String getColToRowXmlStr(InputStream xmlInputStream, String colToRowPath) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    writeColToRowStr(xmlInputStream, byteArrayOutputStream, colToRowPath);
    try {
      return byteArrayOutputStream.toString(CHARSET);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 获取xml行列转换后的字符串.。如果map的key包含xml标签，则key为map的value（同样为map），值插入到该xml标签下
   *
   * @param xmlInputStream xml输入流
   * @param colToRowPath 需要行列转换的xml路径
   * @param specificInputStream 指定值json的inputStream
   */
  public static String getColToRowXmlStrWithSpecificInputStream(InputStream xmlInputStream,
      String colToRowPath, InputStream specificInputStream) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    writeColToRowStrWithSpecificInputStream(xmlInputStream, byteArrayOutputStream, colToRowPath,
        specificInputStream);
    try {
      return byteArrayOutputStream.toString(CHARSET);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 获取xml行列转换后的字符串。如果map的key包含xml标签，则key为map的value（同样为map），值插入到该xml标签下
   *
   * @param xmlInputStream xml输入流
   * @param colToRowPath 需要行列转换的xml路径
   * @param specificMap 指定值map
   */
  public static String getColToRowXmlStrWithSpecificMap(InputStream xmlInputStream,
      String colToRowPath, Map<String, Map<String, String>> specificMap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    writeColToRowStrWithSpecificMap(xmlInputStream, byteArrayOutputStream, colToRowPath,
        specificMap);
    try {
      return byteArrayOutputStream.toString(CHARSET);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getXmlStrWithTransformStrategy(InputStream xmlInputStream, List<AbstractRebuildStrategy> rebuildStrategyList) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    writeXmlWithTransformStrategy(xmlInputStream, byteArrayOutputStream, rebuildStrategyList);
    try {
      return byteArrayOutputStream.toString(CHARSET);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * xml行列转换后结果写入outputStream
   *
   * @param xmlStr xml字符串
   * @param xmlOutputStream xml输出流
   * @param colToRowPath 需要行列转换的xml路径
   */
  public static void writeColToRowStr(String xmlStr, OutputStream xmlOutputStream,
      String colToRowPath) {
    ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    writeColToRowStr(xmlInputStream, xmlOutputStream, colToRowPath);
  }


  /**
   * xml行列转换后结果写入outputStream。如果map的key包含xml标签，则key为map的value（同样为map），值插入到该xml标签下
   *
   * @param xmlStr xml字符串
   * @param xmlOutputStream xml输出流
   * @param colToRowPath 需要行列转换的xml路径
   * @param specificMap 指定值map
   */
  public static void writeColToRowStrWithSpecificMap(String xmlStr, OutputStream xmlOutputStream,
      String colToRowPath, Map<String, Map<String, String>> specificMap) {
    ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    writeColToRowStrWithSpecificMap(xmlInputStream, xmlOutputStream, colToRowPath, specificMap);
  }

  /**
   * xml行列转换后结果写入outputStream。如果map的key包含xml标签，则key为map的value（同样为map），值插入到该xml标签下
   *
   * @param xmlStr xml字符串
   * @param xmlOutputStream xml输出流
   * @param colToRowPath 需要行列转换的xml路径
   * @param specificInputStream 指定值json的inputStream
   */
  public static void writeColToRowStrWithSpecificInputStream(String xmlStr,
      OutputStream xmlOutputStream, String colToRowPath, InputStream specificInputStream) {
    ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    writeColToRowStrWithSpecificInputStream(xmlInputStream, xmlOutputStream, colToRowPath,
        specificInputStream);
  }

  /**
   * xml行列转换后结果写入outputStream
   *
   * @param xmlInputStream xml输入流
   * @param xmlOutputStream xml输出流
   * @param colToRowPath 需要行列转换的xml路径
   */
  public static void writeColToRowStr(InputStream xmlInputStream, OutputStream xmlOutputStream,
      String colToRowPath) {
    writeColToRowStrWithSpecificMap(xmlInputStream, xmlOutputStream, colToRowPath, null);
  }

  /**
   * xml行列转换后结果写入outputStream。如果map的key包含xml标签，则key为map的value（同样为map），值插入到该xml标签下
   *
   * @param xmlInputStream xml输入流
   * @param xmlOutputStream xml输出流
   * @param colToRowPath 需要行列转换的xml路径
   * @param specificInputStream 指定值json的inputStream
   */
  public static void writeColToRowStrWithSpecificInputStream(InputStream xmlInputStream,
      OutputStream xmlOutputStream, String colToRowPath, InputStream specificInputStream) {
    Map<String, Map<String, String>> specificMap = new HashMap<>();

    try {
      JSONObject jsonObject = JSONObject.parseObject(specificInputStream, JSONObject.class);
      for (String key : jsonObject.keySet()) {
        Map<String, String> keyMap = new HashMap<>();
        specificMap.put(key, keyMap);
        JSONObject keyObject = jsonObject.getJSONObject(key);
        for (String key1 : keyObject.keySet()) {
          keyMap.put(key1, keyObject.getString(key1));
        }
      }
      writeColToRowStrWithSpecificMap(xmlInputStream, xmlOutputStream, colToRowPath, specificMap);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * xml行列转换后结果写入outputStream。如果map的key包含xml标签，则key为map的value（同样为map），值插入到该xml标签下
   *
   * @param xmlInputStream xml输入流
   * @param xmlOutputStream xml输出流
   * @param colToRowPath 需要行列转换的xml路径
   * @param specificMap 指定值map
   */
  public static void writeColToRowStrWithSpecificMap(InputStream xmlInputStream,
      OutputStream xmlOutputStream, String colToRowPath,
      Map<String, Map<String, String>> specificMap) {
      AbstractRebuildStrategy strategy ;
      if (specificMap != null) {
        strategy = new XMLRowToColRebuildStrategy(colToRowPath, specificMap);
      } else {
        strategy = new XMLRowToColRebuildStrategy(colToRowPath);
      }
      writeXmlWithTransformStrategy(xmlInputStream, xmlOutputStream, Collections.singletonList(strategy));
  }

  /**
   * 根据转换策略定义xml转换。
   * 最终的xml，会依次按照rebuildStrategyList中的策略，先后执行。
   * @param xmlInputStream
   * @param xmlOutputStream
   * @param rebuildStrategyList
   */
  public static void writeXmlWithTransformStrategy(InputStream xmlInputStream, OutputStream xmlOutputStream, List<AbstractRebuildStrategy> rebuildStrategyList) {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      //2020-03-23 12:32:40 add by gaotx 解决代码扫描发现的XXE问题
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
      SAXParser parser = factory.newSAXParser();
      XMLRebuilder xmlRebuilder = new XMLRebuilder();
      xmlRebuilder.addAllRebuildStrategy(rebuildStrategyList);
      parser.parse(xmlInputStream, xmlRebuilder);
      xmlRebuilder.writeToOutputStream(xmlOutputStream);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void writeSubTagIndexXmlStr(InputStream xmlInputStream, OutputStream xmlOutputStream, String indexTagPath) {
    AbstractRebuildStrategy strategy = new XMLAddIndexRebuildStrategy(indexTagPath);
    writeXmlWithTransformStrategy(xmlInputStream, xmlOutputStream, Collections.singletonList(strategy));
  }

  /**
   * xml字符串转JSONObject（包含标签内属性）
   *
   * @param xmlStr xml字符串
   * @return JSONObject
   */
  public static JSONObject getJSONObjectFromXMLWithAttr(String xmlStr) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    return getJSONObjectFromXMLWithAttr(byteArrayInputStream);
  }

  /**
   * xml输入流转JSONObject（包含标签内属性）
   *
   * @param xmlInputStream xml输入流
   * @return JSONObject
   */
  public static JSONObject getJSONObjectFromXMLWithAttr(InputStream xmlInputStream) {
    return getJSONObjectFromXMLWithAttr(xmlInputStream, null);
  }

  /**
   * xml字符串根据指定的json数组路径，转换JSONObject（包含标签内属性）
   *
   * @param xmlStr xml字符串
   * @param arraySet json数组路径。xml中符合该路径的元素，都会转化为json数组
   * @return JSONObject
   */
  public static JSONObject getJSONObjectFromXMLWithAttr(String xmlStr, Set<String> arraySet) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    return getJSONObjectFromXMLWithAttr(byteArrayInputStream, arraySet);
  }

  /**
   * xml输入流根据指定的json数组路径，转换JSONObject（包含标签内属性）
   *
   * @param xmlInputStream xml输入流
   * @param arraySet json数组路径。xml中符合该路径的元素，都会转化为json数组
   * @return JSONObject
   */
  public static JSONObject getJSONObjectFromXMLWithAttr(InputStream xmlInputStream,
      Set<String> arraySet) {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      //2020-03-23 12:32:40 add by gaotx 解决代码扫描发现的XXE问题
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
      SAXParser parser = factory.newSAXParser();
      Sax2JSONWithAttrHandler handler = new Sax2JSONWithAttrHandler();
      if (arraySet != null) {
        handler.setArrayPathSet(arraySet);
      }
      parser.parse(xmlInputStream, handler);
      return handler.getRoot();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new JSONObject(new LinkedHashMap<>());
  }

  /**
   * 将json对象转换为xml字符串。兼容包含标签/不包含标签的xml转换后json格式
   * @param jsonObject
   * @return
   */
  public static String getXMLStrFromJSONObject(JSONObject jsonObject) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    writeXMLToOutputStreamFromJSONObject(jsonObject, byteArrayOutputStream);
    try {
      return byteArrayOutputStream.toString(CHARSET);
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static void writeXMLToOutputStreamFromJSONObject(JSONObject jsonObject, OutputStream outputStream) {
    SAXTransformerFactory stf=(SAXTransformerFactory) SAXTransformerFactory.newInstance();
    try {
      TransformerHandler handler = stf.newTransformerHandler();
      Transformer transformer = handler.getTransformer();
      transformer.setOutputProperty(
          OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, CHARSET);
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      StreamResult result = new StreamResult(outputStream);
      handler.setResult(result);
      handler.startDocument();
      handler.characters("\n".toCharArray(), 0, 1);
      writeJSONObject(jsonObject, handler, 0);
      handler.endDocument();
    }
    catch (TransformerConfigurationException e) {
      e.printStackTrace();
    }
    catch (SAXException e) {
      e.printStackTrace();
    }
  }

  /**
   * xml字符串转JSONObject（不包含xml标签内属性）
   *
   * @param xmlStr xml字符串
   * @return JSONObject
   */
  public static JSONObject getJSONObjectFromXMLWithoutAttr(String xmlStr) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    return getJSONObjectFromXMLWithoutAttr(byteArrayInputStream);
  }

  /**
   * xml输入流转JSONObject（不包含xml标签内属性）
   *
   * @param xmlInputStream xml输入流
   * @return JSONObject
   */
  public static JSONObject getJSONObjectFromXMLWithoutAttr(InputStream xmlInputStream) {
    return getJSONObjectFromXMLWithoutAttr(xmlInputStream, null);
  }

  /**
   * xml字符串根据指定的json数组路径，转换JSONObject（不包含xml标签内属性）
   *
   * @param xmlStr xml字符串
   * @param arraySet json数组路径。xml中符合该路径的元素，都会转化为json数组
   * @return JSONObject
   */
  public static JSONObject getJSONObjectFromXMLWithoutAttr(String xmlStr, Set<String> arraySet) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        xmlStr.getBytes(Charset.forName(CHARSET)));
    return getJSONObjectFromXMLWithoutAttr(byteArrayInputStream, arraySet);
  }

  /**
   * xml输入流根据指定的json数组路径，转换JSONObject（不包含xml标签内属性）
   *
   * @param xmlInputStream xml输入流
   * @param arraySet json数组路径。xml中符合该路径的元素，都会转化为json数组
   * @return JSONObject
   */
  public static JSONObject getJSONObjectFromXMLWithoutAttr(InputStream xmlInputStream,
      Set<String> arraySet) {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      //2020-03-23 12:32:40 add by gaotx 解决代码扫描发现的XXE问题
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
      SAXParser parser = factory.newSAXParser();
      Sax2JSONWithoutAttrHandler handler = new Sax2JSONWithoutAttrHandler();
      if (arraySet != null) {
        handler.setArrayPathSet(arraySet);
      }
      parser.parse(xmlInputStream, handler);
      return handler.getRoot();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new JSONObject();
  }

  private static void writeJSONObject(JSONObject jsonObject, TransformerHandler handler, int indent) throws SAXException {
    Iterator<Map.Entry<String, Object>> entryIterator = jsonObject.entrySet().iterator();
    while (entryIterator.hasNext()) {
      Map.Entry<String, Object> entry = entryIterator.next();
      if(entry.getValue() instanceof JSONObject) {
        if(((JSONObject) entry.getValue()).containsKey(Sax2JSONWithAttrHandler.ATTR)) {
          AttributesImpl attributes = new AttributesImpl();
          JSONObject attrs = ((JSONObject) entry.getValue()).getJSONObject(
              Sax2JSONWithAttrHandler.ATTR);
          Iterator<Map.Entry<String, Object>> iterator = attrs.entrySet().iterator();
          while (iterator.hasNext()) {
            Map.Entry<String, Object> entry1 = iterator.next();
            attributes.addAttribute("","",entry1.getKey(),"",entry1.getValue().toString());
          }
          handler.startElement("","",entry.getKey(), attributes);
          ((JSONObject) entry.getValue()).remove(Sax2JSONWithAttrHandler.ATTR);
        }
        else {
          handler.startElement("","",entry.getKey(), null);
        }
        if(((JSONObject) entry.getValue()).containsKey(Sax2JSONWithAttrHandler.ARR)) {
          JSONArray jsonArray = ((JSONObject) entry.getValue()).getJSONArray(
              Sax2JSONWithAttrHandler.ARR);
          Iterator<Object> iterator = jsonArray.iterator();
          while (iterator.hasNext()) {
            JSONObject jsonObject1 = (JSONObject) iterator.next();
            writeJSONObject(jsonObject1, handler, indent + 1);
          }
        }
        else if(((JSONObject) entry.getValue()).containsKey(Sax2JSONWithAttrHandler.VALUE)) {
          String value = ((JSONObject) entry.getValue()).getString(Sax2JSONWithAttrHandler.VALUE);
          handler.characters(value.toCharArray(), 0, value.length());
        }
        else {
          writeJSONObject((JSONObject) entry.getValue(), handler, indent + 1);
        }
      }
      else if(entry.getValue() instanceof JSONArray) {
        if(!Sax2JSONWithAttrHandler.ARR.equals(entry.getKey())) {
          handler.startElement("","",entry.getKey(), null);
          Iterator<Object> iterator = ((JSONArray) entry.getValue()).iterator();
          while (iterator.hasNext()) {
            Object object =  iterator.next();
            if(object instanceof JSONObject) {
              writeJSONObject((JSONObject) object, handler, indent + 1);
            }
            else {
              handler.startElement("","",ELEM, null);
              handler.characters(object.toString().toCharArray(), 0, object.toString().length());
              handler.endElement("","", ELEM);
            }
          }
        }
        else {
          Iterator<Object> iterator = ((JSONArray) entry.getValue()).iterator();
          while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof JSONObject) {
              writeJSONObject((JSONObject) object, handler, indent + 1);
            }
          }
        }
      }
      else {
        handler.startElement("","",entry.getKey(), null);
        handler.characters(entry.getValue().toString().toCharArray(), 0, entry.getValue().toString().length());
      }
      handler.endElement("","",entry.getKey());
    }
  }

}
