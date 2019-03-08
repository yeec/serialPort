package bros.manage.dynamic.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import bros.manage.util.PropertiesUtil;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Created by YHYR on 2017-12-25
 */

@Configuration
public class DataSourceConfig {
//    @Value("${spring.datasource.default.ip}")
//    private String defaultDBIp;
//    @Value("${spring.datasource.default.port}")
//    private String defaultDBPort;
//    @Value("${spring.datasource.default.svrName}")
//    private String defaultDBSvrName;
//    @Value("${spring.datasource.default.username}")
//    private String defaultDBUser;
//    @Value("${spring.datasource.default.password}")
//    private String defaultDBPassword;
    @Value("${spring.datasource.default.driver-class-name}")
    private String defaultDBDreiverName;

//    @Value("${spring.datasource.master.url}")
//    private String masterDBUrl;
//    @Value("${spring.datasource.master.username}")
//    private String masterDBUser;
//    @Value("${spring.datasource.master.password}")
//    private String masterDBPassword;
//    @Value("${spring.datasource.default.driver-class-name}")
//    private String masterDBDreiverName;

    @Bean
    public DynamicDataSource dynamicDataSource() throws ConfigurationException {
    	//如果更改数据库配置
		Map<String, Object> dbMap1 = PropertiesUtil.getDBPropertiesDiskInfo();
		String defaultDBIp = (String) dbMap1.get("ip");
		String defaultDBPort = (String) dbMap1.get("port");
		String defaultDBUser = (String) dbMap1.get("username");
		String defaultDBPassword = (String) dbMap1.get("password");
		String defaultDBSvrName = (String) dbMap1.get("svrName");

        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        
        DruidDataSource defaultDataSource = new DruidDataSource();
        defaultDataSource.setUrl("jdbc:oracle:thin:@"+defaultDBIp+":"+defaultDBPort+":"+defaultDBSvrName);
        defaultDataSource.setUsername(defaultDBUser);
        defaultDataSource.setPassword(defaultDBPassword);
        defaultDataSource.setDriverClassName(defaultDBDreiverName);
        defaultDataSource.setInitialSize(5);
        defaultDataSource.setMinIdle(5);
        defaultDataSource.setMaxActive(500);
        defaultDataSource.setMaxWait(6000);
        defaultDataSource.setTimeBetweenEvictionRunsMillis(6000);
        defaultDataSource.setValidationQuery("SELECT 1 FROM DUAL");
        defaultDataSource.setMinEvictableIdleTimeMillis(30000);
        defaultDataSource.setTestWhileIdle(true);
        defaultDataSource.setTestOnBorrow(true);
        defaultDataSource.setTestOnReturn(true);
        defaultDataSource.setPoolPreparedStatements(true);
        defaultDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        defaultDataSource.setRemoveAbandonedTimeout(180);
        defaultDataSource.setRemoveAbandoned(true);

//        DruidDataSource masterDataSource = new DruidDataSource();
//        masterDataSource.setDriverClassName(masterDBDreiverName);
//        masterDataSource.setUrl(masterDBUrl);
//        masterDataSource.setUsername(masterDBUser);
//        masterDataSource.setPassword(masterDBPassword);

        Map<Object,Object> map = new HashMap<>();
        map.put("default", defaultDataSource);
//        map.put("master", masterDataSource);
        dynamicDataSource.setTargetDataSources(map);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:config/mybatis/mapper/*.xml"));
        return bean.getObject();

    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
