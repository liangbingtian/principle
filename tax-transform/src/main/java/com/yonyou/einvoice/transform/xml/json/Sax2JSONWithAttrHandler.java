package com.yonyou.einvoice.transform.xml.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 用于实现xml到json的转换
 */
public class Sax2JSONWithAttrHandler extends DefaultHandler {

  private StringBuilder value = new StringBuilder();

  private JSONObject root;

  public static final String ATTR = "tr_attr";

  public static final String ARR = "tr_arr";

  public static final String VALUE = "tr_val";

  LinkedList<JSONArray> nodeStack = new LinkedList<>();

  Set<String> arrayPathSet = new HashSet<>();

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
    iterJSONObject(root, "");
  }

  /**
   * 解析xml元素
   */
  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
    JSONObject subObject = new JSONObject(new LinkedHashMap<>());
    JSONArray jsonArray = new JSONArray();
    if(attributes != null && attributes.getLength() != 0) {
      JSONObject attrObject = new JSONObject(new LinkedHashMap<>());
      for(int i = 0; i < attributes.getLength(); i++) {
        String name = attributes.getQName(i);
        String value = attributes.getValue(i);
        attrObject.put(name, value);
      }
      subObject.put(ATTR, attrObject);
    }
    jsonObject.put(qName, subObject);
    subObject.put(ARR, jsonArray);
    if(!nodeStack.isEmpty()) {
      nodeStack.peek().add(jsonObject);
    }
    else {
      root = jsonObject;
    }
    nodeStack.push(jsonArray);
    super.startElement(uri, localName, qName, attributes);
  }

  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    JSONArray jsonArray = nodeStack.pop();
    if(jsonArray.size() == 0) {
      jsonArray.add(value.toString());
      value.setLength(0);
    }
    super.endElement(uri, localName, qName);
  }

  @Override
  public void characters(char[] ch, int start, int length)
      throws SAXException {
    super.characters(ch, start, length);
    value.append(new String(ch, start, length).trim());
  }

  public JSONObject getRoot() {
    return root;
  }

  public void setRoot(JSONObject root) {
    this.root = root;
  }

  private void iterJSONObject(JSONObject jsonObject, String path) {
    Iterator<Map.Entry<String, Object>> entryIterator = jsonObject.entrySet().iterator();
    while (entryIterator.hasNext()) {
      Map.Entry<String, Object> entry = entryIterator.next();
      String curPath = "".equals(path)? entry.getKey(): path + "." + entry.getKey();
      if(entry.getValue() instanceof JSONObject) {
        if(((JSONObject) entry.getValue())
            .containsKey(ARR)) {
          JSONArray jsonArray = ((JSONObject) entry.getValue()).getJSONArray(ARR);
          if(!arrayPathSet.contains(curPath)) {
            if (!arrContainsJSONObject(jsonArray)) {
              String content = jsonArray.getString(0);
              JSONObject jsonObject1 = new JSONObject();
              jsonObject1.put(VALUE, content);
              ((JSONObject) entry.getValue()).remove(ARR);
              jsonObject1.putAll((JSONObject) entry.getValue());
              entry.setValue(jsonObject1);
              continue;
            }
            // 如果json数组包含json对象，并且json对象的key之间不重复
            if (!keysEquals(jsonArray)) {
              JSONObject subJSONObject = getJSONObjectFromJSONArray(jsonArray);
              ((JSONObject) entry.getValue()).remove(ARR);
              iterJSONObject(subJSONObject, curPath);
              ((JSONObject) entry.getValue()).putAll(subJSONObject);
              continue;
            }
          }
          Iterator<Object> iterator = jsonArray.iterator();
          while (iterator.hasNext()) {
            iterJSONObject((JSONObject) iterator.next(), curPath);
          }
        }
        iterJSONObject((JSONObject) entry.getValue(), curPath);
      }
    }
  }

  /**
   * 判断json数组中是否包含json对象
   * @param jsonArray
   * @return
   */
  private boolean arrContainsJSONObject(JSONArray jsonArray) {
    if(jsonArray.size() != 0 && jsonArray.get(0) instanceof JSONObject) {
      return true;
    }
    return false;
  }

  /**
   * 从json数组中抽取jsonObject
   * @param jsonArray
   * @return
   */
  private JSONObject getJSONObjectFromJSONArray(JSONArray jsonArray) {
    JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
    Iterator<Object> iterator = jsonArray.iterator();
    while (iterator.hasNext()) {
      JSONObject object = (JSONObject) iterator.next();
      jsonObject.putAll(object);
    }
    return jsonObject;
  }

  /**
   * 判断jsonArray的子jsonObject是否key一致
   * @param jsonArray
   * @return
   */
  public boolean keysEquals(JSONArray jsonArray) {
    boolean equals = false;
    String pre = null;
    Iterator<Object> iterator = jsonArray.iterator();
    while (iterator.hasNext()) {
      JSONObject object = (JSONObject) iterator.next();
      if(object.size() != 1) {
        return false;
      }
      String cur = object.keySet().iterator().next();
      if(cur.equals(pre)) {
        return true;
      }
      else {
        pre = cur;
      }
    }
    return equals;
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
  }

  public void setArrayPathSet(Set<String> arrayPathSet) {
    this.arrayPathSet = arrayPathSet;
  }
}
