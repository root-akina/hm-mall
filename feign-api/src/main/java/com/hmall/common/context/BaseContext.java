package com.hmall.common.context;

public class BaseContext {
    //ThreadLocal是Thread类的一个局部变量，可以在同一个线程之间进行共享
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}