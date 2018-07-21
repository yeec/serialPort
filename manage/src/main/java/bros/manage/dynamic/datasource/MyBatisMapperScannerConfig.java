package bros.manage.dynamic.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * Created by YHYR on 2017-12-25
 */

@Configuration
public class MyBatisMapperScannerConfig {

  @Bean
  public MapperScannerConfigurer mapperScannerConfigurer() {
    MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
    mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
    mapperScannerConfigurer.setBasePackage("bros.manage.business.mapper");
    Properties properties = new Properties();
    properties.setProperty("notEmpty", "false");
    properties.setProperty("IDENTITY", "ORACLE");
    mapperScannerConfigurer.setProperties(properties);
    return mapperScannerConfigurer;
  }
  
}
