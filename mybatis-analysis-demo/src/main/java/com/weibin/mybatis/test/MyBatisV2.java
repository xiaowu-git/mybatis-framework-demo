package com.weibin.mybatis.test;

import com.weibin.mybatis.framework.config.Configuration;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 1、优化MyBatisV1版本，读取properties文件优化为读取xml配置文件
 * 2、用面向过程的思想去优化
 */
public class MyBatisV2 {

    private Configuration configuration = new Configuration();

    private String namespace;

    private boolean isDynamic;

    @Test
    public void test() {
        // 读取全局xml配置文件，并将数据封装到Configuration中
        loadXml("SqlMapConfig.xml");
        //
    }

    /**
     * 加载配置文件
     * @param configLocation
     */
    private void loadXml(String configLocation) {
        // 根据资源路径获取对应的输入流
        InputStream inputStream = getResourceAsStream(configLocation);
        // 将xml流对象转换为Document对象
        Document document = createDocument(inputStream);
        // 按照mybatis语义去解析Document
        parseConfiguration(document.getRootElement());
    }

    /**
     * 解析<configuration/>标签
     * @param configuration
     */
    private void parseConfiguration(Element configuration) {
        Element environments = configuration.element("environments");
        parseEnvironments(environments);
        Element mappers = configuration.element("mappers");
        parseMappers(mappers);
    }

    /**
     * 解析 <mappers/>标签
     * @param mappers
     */
    private void parseMappers(Element mappers) {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element mapper : mapperList) {
            // 获取映射资源路径
            String mapperLocation = mapper.attributeValue("resource");
            InputStream inputStream = getResourceAsStream(mapperLocation);
            Document document = createDocument(inputStream);
            parseMapper(document.getRootElement());
        }
    }

    private void parseMapper(Element mapper) {

    }

    /**
     * 解析<environments/> 标签
     * @param environments
     */
    private void parseEnvironments(Element environments) {
        String aDefault = environments.attributeValue("default");
        List<Element> environmentList = environments.elements("environment");
        for (Element env : environmentList) {
            if (env.attributeValue("id").equals(aDefault)) {
                parseDataSource(env.element("dataSource"));
            }
        }
    }

    /**
     * 解析 <dataSource/> 标签
     * @param dataSource
     */
    private void parseDataSource(Element dataSource) {
        String dataSourceType = dataSource.attributeValue("type");
        if ("DBCP".equals(dataSourceType)) {
            BasicDataSource ds = new BasicDataSource();
            Properties properties = parseProperty(dataSource);
            ds.setDriverClassName(properties.getProperty("driver"));
            ds.setUrl(properties.getProperty("url"));
            ds.setUsername(properties.getProperty("username"));
            ds.setPassword(properties.getProperty("password"));
            configuration.setDataSource(ds);
        }
    }

    /**
     * 解析<dataSource/>标签中的property标签
     * @param dataSource
     * @return
     */
    private Properties parseProperty(Element dataSource) {
        Properties properties = new Properties();
        List<Element> propertyList = dataSource.elements("property");
        for (Element property : propertyList) {
            String name = property.attributeValue("name");
            String value = property.attributeValue("value");
            properties.put(name, value);
        }
        return properties;
    }

    /**
     * 根据文件输入流生成文档对象
     * @param inputStream
     * @return
     */
    private Document createDocument(InputStream inputStream) {
        try {
            SAXReader reader = new SAXReader();
            return reader.read(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取资源路径对应的输入流
     * @param configLocation
     * @return
     */
    private InputStream getResourceAsStream(String configLocation) {
        return this.getClass().getClassLoader().getResourceAsStream(configLocation);
    }

}
