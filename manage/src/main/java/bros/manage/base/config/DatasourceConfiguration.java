package bros.manage.base.config;


import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@Profile("prod")
public class DatasourceConfiguration {
	
	private static final Log logger = LogFactory.getLog(DatasourceConfiguration.class);

	@Bean(name="dataSource")
    @Qualifier(value="dataSource")//限定描述符除了能根据名字进行注入，但能进行更细粒度的控制如何选择候选者
    @Primary//用@Primary区分主数据源
    @ConfigurationProperties(prefix="c3p0")//指定配置文件中，前缀为c3p0的属性值
    public DataSource dataSource(){
        return DataSourceBuilder.create().type(ComboPooledDataSource.class).build();//创建数据源
    }
    /**
    *返回sqlSessionFactory
    */
    @Bean
    @ConfigurationProperties(prefix="mybatis")//指定配置文件中，前缀为c3p0的属性值
    public SqlSessionFactoryBean sqlSessionFactoryBean(){
    	try{
    		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
            sqlSessionFactory.setDataSource(dataSource());
            // 配置类型别名
            sqlSessionFactory.setTypeAliasesPackage("${type-aliases-package}");

            // 配置mapper的扫描，找到所有的mapper.xml映射文件
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("${mapper-locations}");
            sqlSessionFactory.setMapperLocations(resources);

            // 加载全局的配置文件
            sqlSessionFactory.setConfigLocation(
                    new DefaultResourceLoader().getResource("${config-location}"));


            return sqlSessionFactory;
    	}catch (IOException e) {
            logger.warn("mybatis resolver mapper*xml is error");
            return null;
        } catch (Exception e) {
            logger.warn("mybatis sqlSessionFactoryBean create error");
            return null;
        }
        
    }
}
