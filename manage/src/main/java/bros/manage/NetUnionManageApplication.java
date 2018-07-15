package bros.manage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@SpringBootApplication
public class NetUnionManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetUnionManageApplication.class, args);
	}
	
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {  
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();  
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		//附加：处理中文乱码（后期添加）
        List<MediaType> fastMedisTypes=new ArrayList<MediaType>();
        fastMedisTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMedisTypes);
        
		fastConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}
}
