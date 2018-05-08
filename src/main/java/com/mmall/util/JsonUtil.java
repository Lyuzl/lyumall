package com.mmall.util;

import com.mmall.pojo.TestPojo;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * created by Christophe
 * 2018/5/1 下午9:29
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //助记：NON_DEFAULT：default值的没有；NON_NULL：为null的没有；NON_EMPTY：为empty(空)的没有； ALWAYS：全有
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        //忽略空bean转json错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        //所有的日期都统一为以下样式：yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略在json字符串中存在，在java对象中不存在对应属性的情况，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 把对象转化成json字符串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj){
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse Obj to String error: {}", e);
            return null;
        }
    }

    /**
     * 封装格式化好的json字符串返回
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj){
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse Object to String error: {}", e);
            return null;
        }
    }

    /**
     * 把json字符串转成对象
     * @param str  json字符串
     * @param clazz  要反序列化的对象类型
     * @param <T>  泛型
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("Parse String to Object error: {}", e);
            return null;
        }
    }

    //通用字符串转对象
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? (T)str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("Parse String to Object error: {}", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses){

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);

        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("Parse String to Object error: {}", e);
            return null;
        }
    }

    public static void main(String[] args) {
        User u1 = new User();
        u1.setId(3);
        u1.setCreateTime(new Date());
        u1.setEmail("lovecoding@163.com");

        TestPojo testPojo = new TestPojo();
        testPojo.setName("Lyu");
        testPojo.setId(123);
        //{"name":"Lyu","id":123}
        String json = "{\"name\":\"Lyu\",\"id\":123,\"color\":\"red\"}";
        TestPojo testPojoObject = JsonUtil.string2Obj(json, TestPojo.class);


//        String tp = JsonUtil.obj2String(testPojo);

        String user1json = JsonUtil.obj2String(u1);
        String user1jsonPretty = JsonUtil.obj2StringPretty(u1);

//        log.info("user1jsonPretty: {}",user1jsonPretty);
//        log.info("testpojo {}", tp);
//        User user = JsonUtil.string2Obj(user1json, User.class);
//        System.out.println("end");
//
//        List<User> list = new ArrayList<User>();
//        list.add(u1);
//        list.add(u2);
//        String userListStr = JsonUtil.obj2StringPretty(list);
//        System.out.println("==============");
//        log.info(userListStr);
//
//        List<User> userlist1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {
//        });
//        List<User> userlist2 = JsonUtil.string2Obj(userListStr, List.class, User.class);
//        System.out.println("xxxxxxx");
    }
}
