package com.riverside.skeleton.android.base.utils.CollectInfo;

import com.alibaba.fastjson.JSONObject;
import com.riverside.skeleton.android.base.application.BaseApplication;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 收集信息帮助类  1.1
 * b_e  2017/12/10
 * 1.1  2017/12/17 修改单例模式
 */
public class CollectInfoHelper {
    private static BaseApplication application;
    private static volatile CollectInfoHelper instance = null;
    private List<Class<? extends InfoSource>> infoSourceClassList = new ArrayList<>();

    private CollectInfoHelper(BaseApplication app) {
        if (instance != null) {
            throw new RuntimeException("请使用init()取得对象");
        } else {
            application = app;
        }
    }

    /**
     * 初始化对象
     *
     * @param app
     * @return
     */
    public static CollectInfoHelper init(BaseApplication app) {
        if (instance == null) {
            synchronized (CollectInfoHelper.class) {
                if (instance == null) {
                    instance = new CollectInfoHelper(app);
                }
            }
        }
        return instance;
    }

    /**
     * 添加信息源
     *
     * @param infoSourceClasses
     */
    public static void addInfoSource(Class<? extends InfoSource>... infoSourceClasses) {
        if (infoSourceClasses.length > 0) {
            for (Class<? extends InfoSource> item : infoSourceClasses) {
                if (item != null && !instance.infoSourceClassList.contains(item)) {
                    instance.infoSourceClassList.add(item);
                }
            }
        }
    }

    /**
     * 移除信息源
     *
     * @param infoSourceClass
     */
    public static void removeInfoSource(Class<? extends InfoSource> infoSourceClass) {
        if (instance.infoSourceClassList.contains(infoSourceClass)) {
            instance.infoSourceClassList.remove(infoSourceClass);
        }
    }

    /**
     * 收集信息
     *
     * @return
     */
    public static String collectInfo() {
        // 生成json对象用于保存信息
        JSONObject info = new JSONObject();

        for (Class<? extends InfoSource> item : instance.infoSourceClassList) {
            info.put(item.getSimpleName(), getInfo(item));
        }

        return info.toString();
    }

    /**
     * 取得制定信息源的信息
     *
     * @param infoSourceClass
     * @return
     */
    public static JSONObject getInfo(Class<? extends InfoSource> infoSourceClass) {
        try {
            //调用所有已添加的信息源
            Constructor constructor = infoSourceClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            InfoSource infoSource = (InfoSource) constructor.newInstance();
            return infoSource.getInfo(application);
        } catch (InstantiationException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (NoSuchMethodException ignored) {
        } catch (InvocationTargetException ignored) {
        }
        return null;
    }
}
