package com.yonyou.einvoice.transform.xml.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 用于实现xml到json的转换
 */
public class Sax2JSONWithoutAttrHandler extends DefaultHandler {

  private String value;

  private JSONObject root;

  LinkedList<JSONArray> nodeStack = new LinkedList<>();

  Set<String> arrayPathSet = Collections.emptySet();

  private final StringBuilder stringBuilder = new StringBuilder();

  // 遍历树时的模式，用于模式匹配
  private LinkedList<String> pathList = new LinkedList<>();

  /**
   * 用来标识解析开始
   */
  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
  }

  /**
   * 用来标识解析结束
   */
  @Override
  public void endDocument() throws SAXException {
    super.endDocument();
  }

  /**
   * 解析xml元素
   */
  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    JSONArray jsonArray = new JSONArray();
    nodeStack.push(jsonArray);
    pathList.push(qName);
    super.startElement(uri, localName, qName, attributes);
  }

  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    JSONArray array = nodeStack.pop();
    String pathStr = getPathStr();
    pathList.pop();
    JSONObject object = new JSONObject(new LinkedHashMap());
    if (nodeStack.isEmpty()) {
      if (array.size() == 0) {
        JSONObject jsonObject = new JSONObject(new LinkedHashMap());
        jsonObject.put(qName, value);
        value = null;
        root = jsonObject;
      } else {
        if (array.stream().filter(obj -> obj instanceof JSONArray).count() == 0) {
          Set<String> keySet = new HashSet<>();
          array.forEach(currObj -> keySet.addAll(((JSONObject) currObj).keySet()));
          // array子节点无重复元素
          if (!arrayPathSet.contains(pathStr) && keySet.size() == array.size()) {
            JSONObject jsonObject = new JSONObject(new LinkedHashMap());
            for (int i = 0; i < array.size(); i++) {
              JSONObject subObject = array.getJSONObject(i);
              for (String key : subObject.keySet()) {
                jsonObject.put(key, subObject.get(key));
              }
            }
            root = object;
            root.put(qName, jsonObject);
            super.endElement(uri, localName, qName);
            return;
          }
        }
        root = object;
        root.put(qName, array);
      }
    } else {
      if (array.size() == 0) {
        JSONObject jsonObject = object;
        jsonObject.put(qName, value);
        value = null;
        nodeStack.peek().add(jsonObject);
      } else {
        if (!arrayPathSet.contains(pathStr) && array.stream()
            .filter(obj -> obj instanceof JSONArray).collect(Collectors.toSet()).isEmpty()) {
          Set<String> keySet = new HashSet<>();
          array.forEach(currObj -> keySet.addAll(((JSONObject) currObj).keySet()));
          // array子节点无重复元素
          if (keySet.size() == array.size()) {
            JSONObject jsonObject = new JSONObject(new LinkedHashMap());
            for (int i = 0; i < array.size(); i++) {
              JSONObject subObject = array.getJSONObject(i);
              for (String key : subObject.keySet()) {
                jsonObject.put(key, subObject.get(key));
              }
            }
            object.put(qName, jsonObject);
            nodeStack.peek().add(object);
            super.endElement(uri, localName, qName);
            return;
          }
        }
        object.put(qName, array);
        nodeStack.peek().add(object);
      }
    }
    super.endElement(uri, localName, qName);
  }

  @Override
  public void characters(char[] ch, int start, int length)
      throws SAXException {
    super.characters(ch, start, length);
    value = new String(ch, start, length).trim();
  }

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

  public JSONObject getRoot() {
    return root;
  }

  public void setRoot(JSONObject root) {
    this.root = root;
  }

  public Set<String> getArrayPathSet() {
    return arrayPathSet;
  }

  public void setArrayPathSet(Set<String> arrayPathSet) {
    this.arrayPathSet = arrayPathSet;
  }

  public void setArrayPathFromInputStream(InputStream inputStream) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    Set<String> arraypathSet = new HashSet<>();
    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        if (!"".equals(line)) {
          arraypathSet.add(line);
        }
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.setArrayPathSet(arraypathSet);
  }
}
