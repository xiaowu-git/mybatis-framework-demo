package com.weibin.mybatis.test;

import com.weibin.mybatis.framework.config.Configuration;
import com.weibin.mybatis.framework.config.MappedStatement;
import com.weibin.mybatis.framework.sqlnode.SqlNode;
import com.weibin.mybatis.framework.sqlnode.support.IfSqlNode;
import com.weibin.mybatis.framework.sqlnode.support.MixedSqlNode;
import com.weibin.mybatis.framework.sqlnode.support.StaticTextSqlNode;
import com.weibin.mybatis.framework.sqlnode.support.TextSqlNode;
import com.weibin.mybatis.framework.sqlsource.SqlSource;
import com.weibin.mybatis.framework.sqlsource.support.DynamicSqlSource;
import com.weibin.mybatis.framework.sqlsource.support.RawSqlSource;
import com.weibin.mybatis.pojo.User;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        // 执行查询操作
        List<User> users = selectList("queryUserById", "伟斌");
        System.out.println(users);
    }

    private <T> List<T> selectList(String statementId, Object params) {
        List<T> result = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try{
            MappedStatement mappedStatement = configuration.getMappedStatementById(statementId);
            // 获取连接
            connection = getConnection();
            // 获取sql
            String sql = getSql();
            // 获取statement
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数
            setParameters(preparedStatement, params, mappedStatement);
            // 执行查询并获得结果集
            rs = preparedStatement.executeQuery();
            //结果集处理
            handleResultSet(rs, result, mappedStatement);

            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 处理结果集，封装返回结果
     * @param rs
     * @param result
     * @param mappedStatement
     * @param <T>
     */
    private <T> void handleResultSet(ResultSet rs, List<T> result, MappedStatement mappedStatement) {
    }

    /**
     * 设置SQL语句参数
     * @param preparedStatement
     * @param params
     * @param mappedStatement
     */
    private void setParameters(PreparedStatement preparedStatement, Object params, MappedStatement mappedStatement) {
    }

    /**
     * 获取SQL语句
     * @return
     */
    private String getSql() {
        return "";
    }

    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        return configuration.getDataSource().getConnection();
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

    /**
     * 解析<mapper/>标签
     * @param mapper
     */
    private void parseMapper(Element mapper) {
        namespace = mapper.attributeValue("namespace");
        // TODO 获取动态sql标签，如<sql>
        // TODO 获取其他标签
        List<Element> selectElements = mapper.elements("select");
        for (Element selectElement : selectElements) {
            parseStatementElement(selectElement);
        }
    }

    /**
     * 解析<select/>标签
     * @param selectElement
     */
    private void parseStatementElement(Element selectElement) {
        String statementId = selectElement.attributeValue("id");
        if (null == statementId || "".equals(statementId)) {
            return;
        }
        // 一个CRUD标签对应一个MappedStatement对象
        // 一个MappedStatement对象由一个statementId来标识
        statementId = namespace + "." + statementId;

        // 注：parameterType参数可以不设置也可以不解析
        String parameterType = selectElement.attributeValue("parameterType");
        Class<?> parameterTypeClass = resolveType(parameterType);

        String resultType = selectElement.attributeValue("resultType");
        Class<?> resultTypeClass = resolveType(resultType);

        String statementType = selectElement.attributeValue("statementType");
        statementType = statementType == null || "".equals(statementType) ? "prepared" : statementType;

        SqlSource sqlSource = createSqlSource(selectElement);

        MappedStatement mappedStatement = MappedStatement.builder()
                .statementId(statementId)
                .statementType(statementType)
                .parameterTypeClass(parameterTypeClass)
                .resultTypeClass(resultTypeClass)
                .sqlSource(sqlSource)
                .build();
        configuration.addMappedStatement(statementId, mappedStatement);
    }

    private SqlSource createSqlSource(Element selectElement) {
        SqlSource sqlSource = parseScriptNode(selectElement);
        return sqlSource;
    }

    private SqlSource parseScriptNode(Element selectElement) {
        //解析所有的sqlNode
        MixedSqlNode mixedSqlNode = parseDynamicTags(selectElement);
        // 将所有的SqlNode封装到SQLSource中
        SqlSource sqlSource = null;
        if (isDynamic) {
            sqlSource = new DynamicSqlSource(mixedSqlNode);
        }else {
            sqlSource = new RawSqlSource(mixedSqlNode);
        }
        return sqlSource;
    }

    private MixedSqlNode parseDynamicTags(Element selectElement) {
        List<SqlNode> sqlNodes = new ArrayList<>();
        
        int nodeCount = selectElement.nodeCount();
        for (int i = 0; i < nodeCount; i++) {
            Node node = selectElement.node(i);
            if (node instanceof Text) {
                String text = node.getText().trim();
                if (null == text || "".equals(text)) {
                    continue;
                }
                TextSqlNode sqlNode = new TextSqlNode(text);
                if (sqlNode.isDynamic()) {
                    isDynamic = true;
                    sqlNodes.add(sqlNode);
                }else {
                    sqlNodes.add(new StaticTextSqlNode(text));
                }
            }else if (node instanceof Element) {
                isDynamic = true;
                Element element = (Element) node;
                String elementName = element.getName();
                if ("if".equals(elementName)) {
                    String test = element.attributeValue("test");
                    MixedSqlNode mixedSqlNode = parseDynamicTags(element);
                    IfSqlNode ifSqlNode = new IfSqlNode(test, mixedSqlNode);
                    sqlNodes.add(ifSqlNode);
                }else if ("where".equals(elementName)) {
                    // TODO
                }
            }
        }
        return new MixedSqlNode(sqlNodes);
    }

    private Class<?> resolveType(String parameterType) {
        try {
            Class<?> clazz = Class.forName(parameterType);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
