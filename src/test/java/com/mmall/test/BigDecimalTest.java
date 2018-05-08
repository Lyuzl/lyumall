package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by geely
 */




public class BigDecimalTest {

    @Test
    public void test1(){
        System.out.println(0.05+0.01);
        System.out.println(1.0-0.42);
        System.out.println(4.015*100);
        System.out.println(123.3/100);
    }







    @Test
    public void test2(){
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.01);
        System.out.println(b1.add(b2));
    }

    @Test
    public void test3(){
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");
        System.out.println(b1.add(b2));

    }

    @Test
    public void test4(){
        Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;
        System.out.println(f1 == f2);
        System.out.println(f3 == f4);

        Integer t1 = 2;//java在编译的时候,被翻译成-> Integer t1 = Integer.valueOf(2);
        int t2 = 2;
        Integer t3 = null;
        int t4 = 129;
        System.out.println(t1 == t2);
        System.out.println(t3 == t4);
    }

}
