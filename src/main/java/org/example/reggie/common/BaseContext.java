package org.example.reggie.common;

/**
 * ThreadLocal工具类   获取当前用户的id
 */
public class BaseContext
{
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
