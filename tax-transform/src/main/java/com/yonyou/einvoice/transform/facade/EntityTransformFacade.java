package com.yonyou.einvoice.transform.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.entity.MyParserConfig;
import com.yonyou.einvoice.transform.entity.MyTypeUtils;
import com.yonyou.einvoice.transform.iterator.JSONIterator;
import com.yonyou.einvoice.transform.node.CompressedNode;
import com.yonyou.einvoice.transform.node.PatternNode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 实体转换工具类
 * @author liuqiangm
 */
public class EntityTransformFacade {

  /**
   * 将jsonObject转换为clazz类型的对象
   * @param jsonObject
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getTargetObjectFromJSONObject(JSONObject jsonObject, Class<T> clazz) {
    return MyTypeUtils.cast(jsonObject, clazz, MyParserConfig.getGlobalInstance());
  }

  /**
   * 从源json对象中提取出json数组，并转换为目标对象列表
   * @param sourceJSONObject 源json对象
   * @param patternJSONObject 模式json
   * @param clazz 目标对象类型
   * @param <T>
   * @return
   */
  public static <T> List<T> getTargetObjectListFromSourceJSONObject(JSONObject sourceJSONObject,
      JSONObject patternJSONObject,
      Class<T> clazz) {
    PatternNode patternNode = PatternNode.getPatternNodeFromJSONObject(patternJSONObject);
    JSONIterator JSONIterator = new JSONIterator();
    JSONIterator.setPatternNode(patternNode);
    JSONIterator.iterator(sourceJSONObject, "r");
    CompressedNode compressedNode = JSONIterator.getCompressedNode();
    return compressedNode.getTargetJSONObject().getJSONArray("r").toJavaList(clazz);
  }

  /**
   * 从源json数组转换为目标对象数组，并进而转换为目标对象列表
   * @param sourceJSONArray 源json数组
   * @param patternJSONObject 模式json
   * @param clazz 目标对象类型
   * @param <T>
   * @return
   */
  public static <T> List<T> getTargetObjectListFromSourceJSONArray(JSONArray sourceJSONArray,
      JSONObject patternJSONObject,
      Class<T> clazz) {
    PatternNode patternNode = PatternNode.getPatternNodeFromJSONObject(patternJSONObject);
    JSONIterator JSONIterator = new JSONIterator();
    JSONIterator.setPatternNode(patternNode);
    JSONIterator.iterator(sourceJSONArray, "r.[");
    CompressedNode compressedNode = JSONIterator.getCompressedNode();
    JSONObject targetObject = compressedNode.getTargetJSONObject();
    return targetObject.getJSONArray("r").toJavaList(clazz);
  }

  /**
   * 从源json输入流中获取json并转换为目标对象
   * @param sourceInputStream 源json输入流
   * @param patternJSONObject 模式对象
   * @param clazz 目标对象类型
   * @param <T>
   * @return
   */
  public static <T> T getTargetObjectFromSourceJSONInputStream(InputStream sourceInputStream,
      JSONObject patternJSONObject,
      Class<T> clazz) {
    try {
      JSONObject sourceSubJSONObject = new JSONObject(
          JSONObject.parseObject(sourceInputStream, LinkedHashMap.class));
      return getTargetObjectFromSourceJavaObject(sourceSubJSONObject, patternJSONObject, clazz);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 从源json输入流和模式输入流中获取信息，转换为目标对象
   * @param sourceInputStream
   * @param patternInputStream
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getTargetObjectFromSourceJSONInputStream(InputStream sourceInputStream,
      InputStream patternInputStream,
      Class<T> clazz) {
    try {
      JSONObject patternJSONObject = new JSONObject(
          JSONObject.parseObject(patternInputStream, LinkedHashMap.class));
      return getTargetObjectFromSourceJSONInputStream(sourceInputStream, patternJSONObject, clazz);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 从源json字符串和模式json转换为目标对象
   * @param sourceJSONStr
   * @param patternJSONObject
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getTargetObjectFromSourceJSONStr(String sourceJSONStr,
      JSONObject patternJSONObject,
      Class<T> clazz) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        sourceJSONStr.getBytes(Charset.forName("UTF-8")));
    return getTargetObjectFromSourceJSONInputStream(byteArrayInputStream, patternJSONObject, clazz);
  }

  /**
   * 将源对象根据pattern数组转换为目标对象
   * @param sourceObj
   * @param patternJSONObject
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getTargetObjectFromSourceJavaObject(Object sourceObj,
      JSONObject patternJSONObject,
      Class<T> clazz) {
    JSONObject sourceJSONObject = (JSONObject) JSON.toJSON(sourceObj);
    JSONObject targetJSONObject = JSONTransformFacade
        .getJSONObjectFromSourceAndPattern(sourceJSONObject,
            patternJSONObject);
    return MyTypeUtils.cast(targetJSONObject, clazz, MyParserConfig.getGlobalInstance());
  }

  /**
   * 将源对象根据pattern输入流转换为目标对象
   * @param sourceObj
   * @param patternInputStream
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getTargetObjectFromSourceJavaObject(Object sourceObj,
      InputStream patternInputStream,
      Class<T> clazz) {
    try {
      return getTargetObjectFromSourceJavaObject(sourceObj,
          (JSONObject) JSONObject.parseObject(patternInputStream, JSONObject.class), clazz);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将源对象根据模式字符串转换为目标对象
   * @param sourceObj
   * @param patternStr
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getTargetObjectFromSourceJavaObject(Object sourceObj, String patternStr,
      Class<T> clazz) {
    return getTargetObjectFromSourceJavaObject(sourceObj, JSONObject.parseObject(patternStr), clazz);
  }
}
