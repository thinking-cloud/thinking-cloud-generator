package tcg.core.utils;

import tcg.core.config.GeneratorContext;
import tcg.core.enums.PropKey;

/**
 * 生成包名的utils
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class PackageNameUtils {

	
	/**
	 * 获取生成代码的base包名
	 * @return
	 */
	public static String getBasePackage() {
		return GeneratorContext.applicationConfig.getProperty(PropKey.BASE_PACKAGE.propKey());
	}
	
	/**
	 * 获取po对象存放的包名
	 * @return
	 */
	public static String entityPackage() {
		return getBasePackage()+".entity";
	}
	
	/**
	 * 获取bo的包名
	 * @return
	 */
	public static String boPackage() {
		return getBasePackage()+".entity.bo";
	}
	
	/**
	 * api vo的包名
	 * @return
	 */
	public static String apiVoPackage() {
		return getBasePackage()+"api.vo";
	}
	
	/**
	 * api 的包名
	 * @return
	 */
	public static String apiPackage() {
		return getBasePackage()+".api";
	}
	
	/**
	 * server vo的包名
	 * @return
	 */
	public static String voPackage() {
		return getBasePackage()+".server.vo";
	}
	
	/**
	 * controller的包名
	 * @return
	 */
	public static String controllerPackage() {
		return getBasePackage()+".server.controller";
	}
	
	/**
	 * adapter的包名
	 * @return
	 */
	public static String adapterPackage() {
		return getBasePackage()+".server.adapter";
	}
	
	/**
	 * 业务类的包名
	 * @return
	 */
	public static String servicePackage() {
		return getBasePackage()+".server.service";
	}
	
	/**
	 * 业务实现类的包名
	 * @return
	 */
	public static String serviceImplPackage() {
		return getBasePackage()+".server.service.impl";
	}
	
	/**
	 * mapper接口的包名
	 * @return
	 */
	public static String mapperPackage() {
		return getBasePackage()+".server.mapper"; 
	}
	
	/**
	 * mapper xml的包名
	 * @return
	 */
	public static String mapperXmlPackage() {
		return getBasePackage()+".server.mapper.xml";
	}
}
