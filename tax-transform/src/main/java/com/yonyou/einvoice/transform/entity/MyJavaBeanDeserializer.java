package com.yonyou.einvoice.transform.entity;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyJavaBeanDeserializer extends JavaBeanDeserializer {

    public MyJavaBeanDeserializer(ParserConfig config, Class<?> clazz) {
        super(config, clazz);
    }

    public MyJavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type){
        super(config, clazz, type);
    }

    public MyJavaBeanDeserializer(ParserConfig config, JavaBeanInfo beanInfo){
        super(config, beanInfo);
    }

    @Override
    public Object createInstance(Map<String, Object> map, ParserConfig config) //
                                                                               throws IllegalArgumentException,
                                                                               IllegalAccessException,
                                                                               InvocationTargetException {
        Object object = null;

        if (beanInfo.creatorConstructor == null && beanInfo.factoryMethod == null) {
            object = createInstance(null, clazz);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                Object origValue = value;


                FieldDeserializer fieldDeser = smartMatch(key);
                if (fieldDeser == null) {
                    continue;
                }
                final FieldInfo fieldInfo = fieldDeser.fieldInfo;
                Type paramType = fieldInfo.fieldType;
                String format = fieldInfo.format;
                try {
                    if (format != null && paramType == Date.class) {
                        value = MyTypeUtils.castToDate(value, format);
                    } else {
                        value = MyTypeUtils.cast(value, paramType, config);
                    }
                }
                catch (Exception e) {
                    log.error("field transform error, field: '{}', origValue: '{}', target type: '{}' exception stack: ", key, origValue, paramType.getTypeName(), e);
                    value = null;
                }

                fieldDeser.setValue(object, value);
            }

            if (beanInfo.buildMethod != null) {
                Object builtObj;
                try {
                    builtObj = beanInfo.buildMethod.invoke(object);
                } catch (Exception e) {
                    throw new JSONException("build object error", e);
                }

                return builtObj;
            }

            return object;
        }

        
        FieldInfo[] fieldInfoList = beanInfo.fields;
        int size = fieldInfoList.length;
        Object[] params = new Object[size];
        Map<String, Integer> missFields = null;
        for (int i = 0; i < size; ++i) {
            FieldInfo fieldInfo = fieldInfoList[i];
            Object param = map.get(fieldInfo.name);

            if (param == null) {
                Class<?> fieldClass = fieldInfo.fieldClass;
                if (fieldClass == int.class) {
                    param = 0;
                } else if (fieldClass == long.class) {
                    param = 0L;
                } else if (fieldClass == short.class) {
                    param = Short.valueOf((short) 0);
                } else if (fieldClass == byte.class) {
                    param = Byte.valueOf((byte) 0);
                } else if (fieldClass == float.class) {
                    param = Float.valueOf(0);
                } else if (fieldClass == double.class) {
                    param = Double.valueOf(0);
                } else if (fieldClass == char.class) {
                    param = '0';
                } else if (fieldClass == boolean.class) {
                    param = false;
                }
                if (missFields == null) {
                    missFields = new HashMap<String, Integer>();
                }
                missFields.put(fieldInfo.name, i);
            }
            params[i] = param;
        }

        if (missFields != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                FieldDeserializer fieldDeser = smartMatch(key);
                if (fieldDeser != null) {
                    Integer index = missFields.get(fieldDeser.fieldInfo.name);
                    if (index != null) {
                        params[index] = value;
                    }
                }
            }
        }

        if (beanInfo.creatorConstructor != null) {
            try {
                object = beanInfo.creatorConstructor.newInstance(params);
            } catch (Exception e) {
                throw new JSONException("create instance error, "
                                        + beanInfo.creatorConstructor.toGenericString(), e);
            }
        } else if (beanInfo.factoryMethod != null) {
            try {
                object = beanInfo.factoryMethod.invoke(null, params);
            } catch (Exception e) {
                throw new JSONException("create factory method error, " + beanInfo.factoryMethod.toString(), e);
            }
        }
        
        return object;
    }

}
