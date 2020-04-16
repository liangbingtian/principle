package com.yonyou.einvoice.transform.calc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractPathCalcProcessor {

  /**
   * 获取当前path的父path
   *
   * @param path
   * @return
   * @author liuqiangm
   */
  private Optional<String> getParentPath(String path) {
    if (StringUtils.isBlank(path)) {
      return Optional.empty();
    }
    int lastIndexOfDot = path.lastIndexOf(".");
    if (lastIndexOfDot == -1) {
      return Optional.empty();
    }
    return Optional.ofNullable(path.substring(0, lastIndexOfDot));
  }

  /**
   * 获取以relativeObject为基准的，path.substring(parentPath.length)为path的路径对象
   *
   * @param relativeObject
   * @param path
   * @param parentPath
   * @return
   * @author liuqiangm
   */
  private Optional<Object> getObjectFromRelativePath(Object relativeObject, String path,
      String parentPath) {
    if (!path.startsWith(parentPath)) {
      throw new RuntimeException(
          String.format("path: %s与parentPath: %s之间应该是包含关系", path, parentPath));
    }
    String relativePath = path.substring(parentPath.length());
    return Optional.ofNullable(JSONPath.eval(relativeObject, relativePath));
  }


  public void write(Object object) {
    Optional<String> parentTargetPath = getParentPath(getTargetPath());
    if (!parentTargetPath.isPresent()) {
      throw new RuntimeException(String.format("当前目标路径%s不存在父路径", getTargetPath()));
    }
    Optional<Object> parentObject = getObjectFromRelativePath(object, parentTargetPath.get(), "");
    if (!parentObject.isPresent()) {
      return;
    }
    String parentLastNodeStr = getTargetPath().substring(getTargetPath().lastIndexOf(".") + 1);
    if (parentObject.get() instanceof List) {
      JSONArray parentJSONArray = new JSONArray((List) parentObject.get());
      for (Object parentObj : parentJSONArray) {
        if(parentObj instanceof Map && !(parentObj instanceof JSONObject)) {
          parentObj = new JSONObject((Map) parentObj);
        }
        JSONObject parentJSONObject = (JSONObject) parentObj;
        Map<String, Optional<Object>> sourceObjects = new HashMap<>();
        sourceObjects(sourceObjects, parentJSONObject, object, parentTargetPath.get());
        Optional<Object> result = result(sourceObjects);
        result.ifPresent(obj -> parentJSONObject.put(parentLastNodeStr, obj));
      }
    } else if (parentObject.get() instanceof Map) {
      Map parentObjectMap = (Map) parentObject.get();
      JSONObject parentJSONObject = null;
      if(parentObjectMap instanceof JSONObject) {
        parentJSONObject = (JSONObject) parentObjectMap;
      }
      else {
        parentJSONObject = new JSONObject((parentObjectMap));
      }
      Map<String, Optional<Object>> sourceObjects = new HashMap<>();
      sourceObjects(sourceObjects, parentJSONObject, object, parentTargetPath.get());
      Optional<Object> result = result(sourceObjects);
      if(result.isPresent()) {
        parentJSONObject.put(parentLastNodeStr, result.get());
      }
    }
  }

  private void sourceObjects(Map<String, Optional<Object>> sourceObjects, Object parentObject,
      Object object, String parentPath) {
    /**
     * 将sourcePath转换为sourceObject
     * @author liuqiangm
     */
    getSourcePaths().forEach(sourcePath -> {
      if (sourcePath.startsWith(parentPath)) {
        sourceObjects
            .put(sourcePath, getObjectFromRelativePath(parentObject, sourcePath, parentPath));
      } else {
        sourceObjects.put(sourcePath, getObjectFromRelativePath(object, sourcePath, ""));
      }
    });
  }

  public abstract String getTargetPath();

  public abstract List<String> getSourcePaths();

  public abstract Optional<Object> result(Map<String, Optional<Object>> sourceObjects);
}
