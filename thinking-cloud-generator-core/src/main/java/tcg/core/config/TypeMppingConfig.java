package tcg.core.config;

import java.io.InputStream;
import java.util.Properties;

import tcg.core.exception.GeneratorException;

/**
 * typemapping.properties配置文件的加载类
 * 用于程序映射配置
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class TypeMppingConfig {
	public Properties typeMappingProp = new Properties();
	
	public TypeMppingConfig() {
		try(InputStream in = ClassLoader.getSystemResourceAsStream("typemapping.properties")){
			typeMappingProp.load(in);
		}catch (Exception e) {
			throw new GeneratorException("读取配置文件失败! ",e);
		}
	}
	
	/**
	 * 根据数据库类型获取java类型
	 * @param databaseType
	 * @return
	 */
	public String getJavaType(String databaseType) {
		return typeMappingProp.getProperty(databaseType);
	}
}
