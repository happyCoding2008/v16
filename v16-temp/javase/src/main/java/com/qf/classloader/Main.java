package com.qf.classloader;

/**
 * @author huangguizhao
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        //类加载机制
        String string = new String();

        Class<?> clazz = Class.forName("com.qf.classloader.Student");
        ClassLoader classLoader = clazz.getClassLoader();
        System.out.println(classLoader.getClass().getSimpleName());

        while(classLoader.getParent() != null){
            classLoader = classLoader.getParent();
            System.out.println("-->"+classLoader.getClass().getSimpleName());
        }
    }
}
