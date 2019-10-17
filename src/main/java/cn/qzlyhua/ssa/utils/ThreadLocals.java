package cn.qzlyhua.ssa.utils;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author qzlyhua
 */
public class ThreadLocals {
    public static ThreadLocal<Integer> TIMES = new ThreadLocal<>();
    public static ThreadLocal<Integer> TIMES_LH = new ThreadLocal<>();
    /**
     * 移除所有ThreadLocal里的变量，防止内存泄漏
     */
    public static void removeAll() {
        try {
            Class<ThreadLocals> clazz = ThreadLocals.class;
            List<Field> allFieldsList = FieldUtils.getAllFieldsList(clazz);
            for (Field field : allFieldsList) {
                ThreadLocal object = (ThreadLocal) field.get(clazz);
                object.remove();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
