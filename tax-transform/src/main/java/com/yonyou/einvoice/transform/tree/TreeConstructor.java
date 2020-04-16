package com.yonyou.einvoice.transform.tree;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 用于根据父id节点，id节点，子节点字段，构建树列表
 *
 * @author liuqiangm
 */
public class TreeConstructor {

  private static Map<String, Map<String, Field>> classFieldMap = new ConcurrentHashMap<>();

  /**
   * 用于将对象列表，根据id字段、父id字段和子id字段，转换成目标类型的对象树列表
   *
   * @param objectList    对象列表
   * @param idField       id字段名称
   * @param parentidField 父id字段名称
   * @param sonField      子id字段名称
   * @param clazz         目标对象的类型
   * @param <T>
   * @return
   */
  public static <T> List<T> getTreeListFromSourceObjectList(List<T> objectList, String idField,
      String parentidField, String sonField, Class<T> clazz) {
    String clazzName = clazz.getName();
    Map<String, Field> fieldMap = getAndAddFieldMap(idField, parentidField, sonField, clazz);
    Map<Object, T> id2JSONObjectMap = getField2JSONObjectMap(objectList, idField, fieldMap);
    Map<Object, List<T>> parentid2JSONObjectListMap = getField2JSONObjectListMap(objectList,
        parentidField, fieldMap);
    List<T> targetObjectList = getTreeListFromIdMapAndParentidListMap(id2JSONObjectMap,
        parentid2JSONObjectListMap, sonField, fieldMap);
    return targetObjectList;
  }

  /**
   * 获取clazz的字段Field映射
   *
   * @param idField
   * @param parentidField
   * @param sonField
   * @param clazz
   * @return
   */
  private static Map<String, Field> getAndAddFieldMap(String idField, String parentidField,
      String sonField, Class clazz) {
    if (!classFieldMap.containsKey(clazz.getName())) {
      Map<String, Field> fieldMap = new HashMap<>();
      try {
        Field field1 = clazz.getDeclaredField(idField);
        Field field2 = clazz.getDeclaredField(parentidField);
        Field field3 = clazz.getDeclaredField(sonField);
        field1.setAccessible(true);
        field2.setAccessible(true);
        field3.setAccessible(true);
        fieldMap.put(idField, field1);
        fieldMap.put(parentidField, field2);
        fieldMap.put(sonField, field3);
        classFieldMap.put(clazz.getName(), fieldMap);
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
      return fieldMap;
    }
    return classFieldMap.get(clazz.getName());
  }

  /**
   * 根据id映射和父id映射，获取最终构建好的json对象列表
   *
   * @param idMap       id映射
   * @param parentidMap 父id映射
   * @param sonField    json对象中，用于存储下级子节点的字段名称
   * @return
   */
  private static <T> List<T> getTreeListFromIdMapAndParentidListMap(Map<Object, T> idMap,
      Map<Object, List<T>> parentidMap, String sonField, Map<String, Field> fieldMap) {
    // 用于保留最终的所有树根节点。
    Set<T> resultSet = new HashSet<>(new Double(idMap.size() / 0.7).intValue());
    resultSet.addAll(idMap.values());
    for (Map.Entry<Object, List<T>> parentidMapEntry : parentidMap.entrySet()) {
      Object parentid = parentidMapEntry.getKey();
      T parentObject = idMap.get(parentid);
      /**
       * 判断父json对象是否存在。如果不存在，则继续下一次遍历
       */
      if (parentObject == null) {
        continue;
      }
      /**
       * 需要考虑，parentJSONObject可能存在key不为空，但value为空的情况
       */
      List<T> sonList = getAndAddSonFieldList(fieldMap.get(sonField), parentObject);
      for (T sonObject : parentidMapEntry.getValue()) {
        sonList.add(sonObject);
        resultSet.remove(sonObject);
      }
    }
    return new ArrayList<>(resultSet);
  }

  private static <T> List<T> getAndAddSonFieldList(Field sonField, T parentObject) {
    try {
      List<T> sonList = (List<T>) sonField.get(parentObject);
      if (sonList == null) {
        sonList = new ArrayList<>();
        sonField.set(parentObject, sonList);
      }
      return sonList;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static <T> Map<Object, T> getField2JSONObjectMap(List<T> objectList, String field,
      Map<String, Field> fieldMap) {
    Map<Object, T> resultMap = new HashMap<>(new Double(objectList.size() / 0.7).intValue());
    for (T object : objectList) {
      try {
        Object key = fieldMap.get(field).get(object);
        resultMap.put(key, object);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return resultMap;
  }

  private static <T> Map<Object, List<T>> getField2JSONObjectListMap(List<T> objectList,
      String field, Map<String, Field> fieldMap) {
    Map<Object, List<T>> resultMap = new HashMap<>(new Double(objectList.size() / 0.7).intValue());
    for (T object : objectList) {
      try {
        Object key = fieldMap.get(field).get(object);
        resultMap.putIfAbsent(key, new ArrayList<>());
        resultMap.get(key).add(object);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return resultMap;
  }
}
