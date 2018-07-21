package bros.manage.dynamic.datasource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by YHYR on 2017-12-25
 */

@SpringBootApplication
public class DynamicApplication /*implements CommandLineRunner*/ {
//	@Autowired
//	UserService userService;
//
//	@Autowired
//	DBService dbService;
//
//	@Autowired
//	StudentService studentService;
//
//	public static void main(String[] args) {
//		SpringApplication.run(DynamicApplication.class, args);
//	}
//
//	@Override
//	public void run(String... strings) throws Exception {
//
//		DruidDataSource dynamicDataSource = new DruidDataSource();
//		dynamicDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//		dynamicDataSource.setUrl("jdbc:oracle:thin:@"+ip+":"+port+":"+svrName);
//		dynamicDataSource.setUsername(userName);
//		dynamicDataSource.setPassword(passWord);
//
//		/**
//		 * 创建动态数据源
//		 */
//		Map<Object, Object> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();
//		dataSourceMap.put("dynamic-slave", dynamicDataSource);
//		DynamicDataSource.getInstance().setTargetDataSources(dataSourceMap);
//		/**
//		 * 切换为动态数据源实例，打印学生信息
//		 */
//		DataSourceContextHolder.setDBType("dynamic-slave");
//		List<StudentInfo> studentInfoList = studentService.getStudentInfo();
//		studentInfoList.stream().forEach(studentInfo -> System.out.println("studentName is : " + studentInfo.getStudentName() + "; className is : " + studentInfo.getClassName() + "; gradeName is : " + studentInfo.getGradeName()));
//
//	}
}
