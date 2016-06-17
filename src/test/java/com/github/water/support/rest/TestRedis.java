/**
* @author weiwei.Wang
* @date 2016年6月15日 
* @todo TODO
*2016年6月15日
*/ 
package com.github.water.support.rest; 

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class TestRedis {
    private Jedis jedis; 
    
    @Before
    public void setup() {
        //连接redis服务器，192.168.0.100:6379
        jedis = new Jedis("172.16.40.40", 6379);
    }
    
    @Test
    public void testAdd() {
        jedis.set("cas:name".getBytes(),"xinxin".getBytes());//向key-->name中放入了value-->xinxin  
        System.out.println(jedis.get("name".getBytes()));//执行结果：xinxin  
        
    }
    
    @Test
    public void testDel() {
    	jedis.del("cas:name".getBytes());  //删除某个键
        System.out.println(jedis.get("cas:name".getBytes()));
    }
    
//    /** 
//     * jedis操作List 
//     */  
//    @Test  
//    public void testList(){  
//        //开始前，先移除所有的内容  
//        jedis.del("java framework");  
//        System.out.println(jedis.lrange("java framework",0,-1));  
//        //先向key java framework中存放三条数据  
//        jedis.lpush("java framework","spring");  
//        jedis.lpush("java framework","struts");  
//        jedis.lpush("java framework","hibernate");  
//        //再取出所有数据jedis.lrange是按范围取出，  
//        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有  
//        System.out.println(jedis.lrange("java framework",0,-1));  
//        
//        jedis.del("java framework");
//        jedis.rpush("java framework","spring");  
//        jedis.rpush("java framework","struts");  
//        jedis.rpush("java framework","hibernate"); 
//        System.out.println(jedis.lrange("java framework",0,-1));
//    }  
//    
//    /** 
//     * jedis操作Set 
//     */  
//    @Test  
//    public void testSet(){  
//        //添加  
//        jedis.sadd("user","liuling");  
//        jedis.sadd("user","xinxin");  
//        jedis.sadd("user","ling");  
//        jedis.sadd("user","zhangxinxin");
//        jedis.sadd("user","who");  
//        //移除noname  
//        jedis.srem("user","who");  
//        System.out.println(jedis.smembers("user"));//获取所有加入的value  
//        System.out.println(jedis.sismember("user", "who"));//判断 who 是否是user集合的元素  
//        System.out.println(jedis.srandmember("user"));  
//        System.out.println(jedis.scard("user"));//返回集合的元素个数  
//    }  
//  
//    @Test  
//    public void test() throws InterruptedException {  
//        //jedis 排序  
//        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）  
//        jedis.del("a");//先清除数据，再加入数据进行测试  
//        jedis.rpush("a", "1");  
//        jedis.lpush("a","6");  
//        jedis.lpush("a","3");  
//        jedis.lpush("a","9");  
//        System.out.println(jedis.lrange("a",0,-1));// [9, 3, 6, 1]  
//        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果  
//        System.out.println(jedis.lrange("a",0,-1));  
//    }  
//    
//    @Test
//    public void testRedisPool() {
//        RedisUtil.getJedis().set("newname", "中文测试");
//        System.out.println(RedisUtil.getJedis().get("newname"));
//    }
}
 