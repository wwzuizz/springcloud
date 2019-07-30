package com.linwen.comm.entity;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Properties;


//@GenericGenerator支持13种策略，分别是：
// static {
//		GENERATORS.put("uuid", UUIDHexGenerator.class);
//		GENERATORS.put("hilo", TableHiLoGenerator.class);
//		GENERATORS.put("assigned", Assigned.class);
//		GENERATORS.put("identity", IdentityGenerator.class);
//		GENERATORS.put("select", SelectGenerator.class);
//		GENERATORS.put("sequence", SequenceGenerator.class);
//		GENERATORS.put("seqhilo", SequenceHiLoGenerator.class);
//		GENERATORS.put("increment", IncrementGenerator.class);
//		GENERATORS.put("foreign", ForeignGenerator.class);
//		GENERATORS.put("guid", GUIDGenerator.class);
//		GENERATORS.put("uuid.hex", UUIDHexGenerator.class); //uuid.hex is deprecated
//		GENERATORS.put("sequence-identity", SequenceIdentityGenerator.class);
//		}

/**
 * 1558576873   秒前10位
 * 233          毫秒三位
 * 1001         自增四位
 * 2019         当前的年
 * 以2019为58576873中间连接毫秒233在自增三位
 * 58576873会一直大,直到第三年才会重复年，但是第二年2019变为2020所以能保证不会重复
 * id生成规则：当前年份4+毫秒时间戳去掉头2位+自增4位 共19位
 * 以毫秒内可以生成0到9999也就是9999条记录
 */
public class IdGenerator implements Configurable, IdentifierGenerator {


    public static final String IDGENERATOR = "IdGenerator";
    public static final String IDGENERATORCLASS = "com.linwen.comm.entity.IdGenerator";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
    /**
     * 20位末尾的数字id
     */
    public static int Guid = 0;

    public static Long getGuid() {
        long now = System.currentTimeMillis();
        //获取4位年份数字
        //获取时间戳
        String time = dateFormat.format(now);
        String info = now + "";
        //获取三位随机数
        //int ran=(int) ((Math.random()*9+1)*100);
        //要是一段时间内的数据连过大会有重复的情况，所以做以下修改
        String ran = "";
        if (Guid > 9999) {
            Guid = 0;
        }
        if (Guid < 10) {
            ran = "000" + Guid;
        } else if (Guid < 100) {
            ran = "00" + Guid;
        } else if (Guid < 1000) {
            ran = "0" + Guid;
        } else {
            ran = Guid + "";
        }
        Guid += 1;
        return Long.valueOf(time + info.substring(2) + ran);
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {

    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return getGuid();
    }

}
 
 
