package dao;

import mapper.AnnotationMapper;
import mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * mybatis demo 测试
 *
 * 1、dao开发方式：
 *  1.编写全局配置文件SqlMapConfig.xml
 *  2.编写Mapper映射文件
 *  3.编写pojo类
 *  4.编写dao接口及实现类
 *
 * 2、mapper代理开发方式：
 *
 */
public class MybatisDemoTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception {
        SqlSessionFactoryBuilder sessionFactoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory = sessionFactoryBuilder.build(Resources.getResourceAsStream("SqlMapConfig.xml"));
    }

    @Test
    public void findUserById() throws Exception {
        UserDao userDao = new UserDaoImpl(sqlSessionFactory);
        User user = userDao.findUserById(1);
        System.out.println(user);
    }

    @Test
    public void findUserByName() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.findUserByName("斌");
        System.out.println("findUserByName -> " + user);
        sqlSession.close();
    }

    @Test
    public void insertUser() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AnnotationMapper mapper = sqlSession.getMapper(AnnotationMapper.class);
        User user = new User();
        user.setName("王五");
        user.setAge(18);
        user.setBirthday(new Date());
        user.setAddress("广东深圳");
        mapper.insert(user);
        System.out.println(user);
    }

}