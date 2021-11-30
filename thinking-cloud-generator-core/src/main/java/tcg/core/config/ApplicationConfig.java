package tcg.core.config;

import java.io.InputStreamReader;
import java.util.Properties;

import tcg.core.exception.GeneratorException;

/**
 * application.properties配置文件加载类
 * 用于程序的配置
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class ApplicationConfig {
	private Properties prop = new Properties();
	
	public ApplicationConfig() {
		try(InputStreamReader in = new InputStreamReader(ClassLoader.getSystemResourceAsStream("application.Properties"),"UTF-8")){
			prop.load(in);
		}catch (Exception e) {
			throw new GeneratorException("读取配置文件失败! ",e);
		}
	}

	
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
}
