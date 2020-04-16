package com.yonyou.einvoice.transform.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.iterator.JSONIterator;
import com.yonyou.einvoice.transform.node.CompressedNode;
import com.yonyou.einvoice.transform.node.PatternNode;
import com.yonyou.einvoice.transform.tree.TreeConstructor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * json转换工具类
 * @author liuqiangm
 */
@Slf4j
public class JSONTransformFacade {

  /**
   * 为json中的所有数组添加索引，将其中所有的json数组转换为json对象
   * @param jsonObject
   * @return
   */
  public static JSONObject getJSONObjectWithIndex(JSONObject jsonObject) {
    for(Map.Entry<String, Object> entry : jsonObject.entrySet()) {
      if(entry.getValue() instanceof JSONArray) {
        entry.setValue(getJSONObjectFromJSONArray((JSONArray) entry.getValue()));
      }
      if(entry.getValue() instanceof JSONObject) {
        getJSONObjectWithIndex((JSONObject) entry.getValue());
      }
    }
    return jsonObject;
  }

  /**
   * 将源json输入流根据模式字符串转换为目标json对象
   * @param sourceInputStream
   * @param patternStr
   * @return
   */
  public static JSONObject getJSONObjectFromSourceAndPattern(InputStream sourceInputStream,
      String patternStr) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(patternStr.getBytes(
        Charset.forName("UTF-8")));
    return getJSONObjectFromSourceAndPattern(sourceInputStream, byteArrayInputStream);
  }

  /**
   * 将源json字符串根据模式字符串转换为目标json对象
   * @param sourceStr
   * @param patternStr
   * @return
   */
  public static JSONObject getJSONObjectFromSourceAndPattern(String sourceStr, String patternStr) {
    return getJSONObjectFromSourceAndPattern(new ByteArrayInputStream(sourceStr.getBytes(
        Charset.forName("UTF-8"))), new ByteArrayInputStream(patternStr.getBytes(
        Charset.forName("UTF-8"))));
  }

  /**
   * 将源json输入流根据模式输入流转换为目标json对象
   * @param sourceInputStream
   * @param patternInputStream
   * @return
   */
  public static JSONObject getJSONObjectFromSourceAndPattern(InputStream sourceInputStream,
      InputStream patternInputStream) {
    try {
      return getJSONObjectFromSourceAndPattern(
          new JSONObject(JSONObject.parseObject(sourceInputStream,
              LinkedHashMap.class)),
          (JSONObject) JSONObject.parseObject(patternInputStream, JSONObject.class));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将源json对象根据模式输入流转换为目标json对象
   * @param sourceJSONObject
   * @param patternInputStream
   * @return
   */
  public static JSONObject getJSONObjectFromSourceAndPattern(JSONObject sourceJSONObject,
      InputStream patternInputStream) {
    try {
      JSONObject patternJSONObject = JSONObject.parseObject(patternInputStream, JSONObject.class);
      return getJSONObjectFromSourceAndPattern(sourceJSONObject, patternJSONObject);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将java对象转换为相应的json数组。使用inputstream表示转换规则
   * @param sourceObject
   * @param patternInputStream
   * @return
   */
  public static JSONObject getJSONObjectFromSourceAndPattern(Object sourceObject, InputStream patternInputStream) {
    JSONObject jsonObject = (JSONObject) JSON.toJSON(sourceObject);
    return getJSONObjectFromSourceAndPattern(jsonObject, patternInputStream);
  }

  /**
   * 将java对象转换为相应的json数组。使用字符串表示转换规则
   * @param sourceObject
   * @param patternStr
   * @return
   */
  public static JSONObject getJSONObjectFromSourceAndPattern(Object sourceObject, String patternStr) {
    try {
      ByteArrayInputStream patternInputStream = new ByteArrayInputStream(
          patternStr.getBytes("UTF-8"));
      return getJSONObjectFromSourceAndPattern(sourceObject, patternInputStream);
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将源json对象根据模式json对象转换为目标json对象
   * @param sourceJSONObject
   * @param patternJSONObject
   * @return
   */
  public static JSONObject getJSONObjectFromSourceAndPattern(JSONObject sourceJSONObject,
      JSONObject patternJSONObject) {
    JSONObject resultObject = null;
    revisePatternJSONObject(patternJSONObject, "", "");
    try {
      PatternNode patternNode = PatternNode.getPatternNodeFromJSONObject(patternJSONObject);
      JSONIterator jSONIterator = new JSONIterator();
      jSONIterator.setPatternNode(patternNode);
      jSONIterator.iterator(sourceJSONObject, "r");
      CompressedNode compressedNode = jSONIterator.getCompressedNode();
      resultObject = compressedNode.getTargetJSONObject();
      if (resultObject == null) {
        return new JSONObject();
      } else {
        return resultObject.getJSONObject("r");
      }
    }
    catch (Exception e) {
      log.error("transform error. sourceObj:{}, targetObj:{}, message:{}, stack:{}",
          sourceJSONObject, resultObject, e.getMessage(), e);
      return new JSONObject();
    }
  }

  public static void revisePatternJSONObject(JSONObject patternJSONObject, String parentSourcePath,
      String parentTargetPath) {
    if (patternJSONObject == null) {
      return;
    }
    Iterator<Map.Entry<String, Object>> entryIterator = patternJSONObject.entrySet().iterator();
    List<Object[]> objList = new ArrayList<>();
    while (entryIterator.hasNext()) {
      Map.Entry<String, Object> entry = entryIterator.next();
      String key = entry.getKey();
      Object value = entry.getValue();
      // 判断是否有源 -> 目标映射，还是直接目标映射
      int index = key.indexOf("->");
      String sourcePath = parentSourcePath;
      String targetPath = parentTargetPath;
      // 直接目标映射
      if (index == -1) {
        // 如果当前目标包含相对路径，则将上级和当前相对路径进行拼接，并删除JSONObject中的当前路径节点
        if (key.startsWith("./")) {
          targetPath = parentTargetPath + key.substring(2);
          objList.add(new Object[]{targetPath, value});
          entryIterator.remove();
        }
        // 当前目标不包含相对路径，则赋值targetPath为当前key
        else {
          targetPath = key;
        }
      }
      // 直接源 -> 目标映射
      else {
        String key1 = key.substring(0, index);
        String key2 = key.substring(index + 2);
        // 源路径为相对路径，则取上级路径，拼接处当前路径的绝对路径
        if (key1.startsWith("./")) {
          sourcePath = parentSourcePath + key1.substring(2);
        }
        // 源路径为绝对路径，则赋值sourcePath为当前路径
        else {
          sourcePath = key1;
        }
        // 目标路径为相对路径，则取上级路径，拼接出当前路径的绝对路径
        if (key2.startsWith("./")) {
          targetPath = parentTargetPath + key2.substring(2);
        }
        // 目标路径为绝对路径，则赋值targetPath为当前路径
        else {
          targetPath = key2;
        }
        // 判断当前匹配节点是否存在相对路径，如果存在相对路径，则删除节点，并保存新的匹配节点，用于最后统一添加
        if (key1.startsWith("./") || key2.startsWith("./")) {
          objList.add(new Object[]{sourcePath + "->" + targetPath, value});
          entryIterator.remove();
        }
      }
      // 递归处理子匹配节点
      if (value instanceof JSONObject) {
        revisePatternJSONObject((JSONObject) value, sourcePath, targetPath);
      }
    }
    // 如果当前节点非空，则打印。
    if (!objList.isEmpty()) {
      for (Object[] pair : objList) {
        patternJSONObject.put(pair[0].toString(), pair[1]);
      }
    }
  }

  private static JSONObject getJSONObjectFromJSONArray(JSONArray jsonArray) {
    JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
    for(int i = 0; i < jsonArray.size(); i++) {
      String key = "_" + i;
      Object obj = jsonArray.get(i);
      if(obj instanceof JSONArray) {
        jsonObject.put(key, getJSONObjectFromJSONArray((JSONArray) obj));
        continue;
      }
      if(obj instanceof JSONObject) {
        getJSONObjectWithIndex((JSONObject) obj);
      }
      jsonObject.put(key, obj);
    }
    return jsonObject;
  }

  /**
   * 根据指定某个字段的值为父节点id，将java对象列表转化为树形森林结构
   * @param objectList 对象列表
   * @param idField 对象中的主键字段
   * @param parentidField 对象中的父对象主键字段
   * @param sonField 对象中的子对象列表字段
   * @param clazz 对象类型
   * @param <T>
   * @return
   */
  public static <T> List<T> getTreeListFromSourceObjectList(List<T> objectList, String idField, String parentidField, String sonField, Class<T> clazz) {
    return TreeConstructor
        .getTreeListFromSourceObjectList(objectList, idField, parentidField, sonField, clazz);
  }

}
