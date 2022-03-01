package com.atguigu;

import com.sun.jmx.snmp.agent.SnmpMibOid;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.bcel.internal.classfile.SourceFile;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.awt.*;
import java.util.Random;

public class PhoneCode {

    public static void main(String[] args) {}

    @Test
    public void test1(){
        String phoneId = "15726885608";
        String code = getCode();
        setCode(phoneId, code);
        jiaoyan(phoneId, code);
    }



    /**
     * 1. 生成6位的验证码
     */
    public String getCode(){
        Random random = new Random();
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int j = random.nextInt(10);
            code.append(j);
        }
        System.out.println(code.toString());
        return code.toString();
    }

    /**
     * 2. 保存到 redis，并且每个号码每次只能发送三次验证码
     */
    public void setCode(String phoneId, String code){
        /**
         * 手机号码区分
         * codeKey 验证码key
         * countKey 发送次数key
         */
        String codeKey = "yanzhengma"+phoneId+":code";
        String countKey = "yanzhengma"+phoneId+":count";
        Jedis jedis = getJedis();
        String count = jedis.get(countKey);
        System.out.println("contKey= " + count);
        /**
         * 每个手机只能发送3次
         */
        if (count==null){
            // 创建键值对和过期时间24小时
            jedis.setex(countKey, 24*60*60, "1");
            System.out.println("=null-----" + jedis.get(countKey));
        } else if (Integer.parseInt(count)<3){
            // 不足3次的，value+1
            jedis.incr(countKey);
            System.out.println("<3-----" + jedis.get(countKey));
        } else if (Integer.parseInt(count)>2){
            // 超过3次的关闭数据库连接
            System.out.println("发送超过3次");
            jedis.close();
            return;
        }

        /**
         * 获取验证码，并放入redis 过期时间 120s
         */
        jedis.setex(codeKey, 120, code);
        jedis.close();
    }

    /**
     * 验证码校验
     */
    public void jiaoyan(String phoneId, String code){
        String codeKey = "yanzhengma"+phoneId+":code";
        Jedis jedis = getJedis();
        String rcode = jedis.get(codeKey);
        if (rcode.equals(code)){
            System.out.println("ok");
        }else {
            System.out.println("fail");
        }
    }

    /**
     * 获取jedis
     */
    public Jedis getJedis(){
        return new Jedis("192.168.188.100",6379);
    }

}
