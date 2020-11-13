package com.riverside.skeleton.android.base.utils.CollectInfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.riverside.skeleton.android.util.log.CLog;

import java.lang.reflect.Field;

public class ObjectFieldUtils {
    /**
     * 将Field数组转换为JSONObject
     *
     * @param fields
     * @return
     */
    public static JSONObject toJSONObject(Field[] fields) {
        JSONObject infos = new JSONObject();

        for (Field field : fields) {
            try {
                // 设置信息为可见
                field.setAccessible(true);
                Object value = field.get(null); // 取变量的值
                if (value.getClass().isArray()) {
                    Object[] arr = (Object[]) value; // 装换成数组
                    // 保存相关信息
                    infos.put(field.getName(), JSONArray.toJSON(arr));
                } else {
                    // 保存相关信息
                    infos.put(field.getName(), value.toString());
                }
            } catch (Exception e) {
                CLog.e("an error occurred when collect crash info", e);
            }
        }
        return infos;
    }
}