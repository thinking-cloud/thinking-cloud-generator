package tcg.core.utils;

import java.io.File;

import tcg.core.config.ApplicationConfig;
import tcg.core.config.GeneratorContext;
import tcg.core.enums.PropKey;

/**
 * 文件路径处理类
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class FilePathUtils {
	public static final String MAIN_JAVA_PATH = "src/main/java";
	public static final String MAIN_RESOURCES_PATH = "src/main/resources";
	public static final String TEST_JAVA_PATH = "src/test/java";
	public static final String TEST_RESOURCES_PATH = "src/test/resources";
	public static final String TARGET_PATH = "target";
	
	/**
	 * Maven项目路径
	 * @return
	 */
	public static File projectPath() {
		ApplicationConfig config = GeneratorContext.applicationConfig;
		String projectPath = config.getProperty(PropKey.PROJECT_PATH.propKey());
		String projectName =  config.getProperty(PropKey.PROJECT_NAME.propKey());
		return new File(projectPath,projectName);
	}
	
	/**
	 * api moudle路径
	 * @return
	 */
	public static File apiPath() {
		ApplicationConfig config = GeneratorContext.applicationConfig;
		String projectName =  config.getProperty(PropKey.PROJECT_NAME.propKey());
		return new File(projectPath(),projectName+"-api");
	}
	
	/**
	 * beans moudle路径
	 * @return
	 */
	public static File beansPath() {
		ApplicationConfig config = GeneratorContext.applicationConfig;
		String projectName =  config.getProperty(PropKey.PROJECT_NAME.propKey());
		return new File(projectPath(),projectName+"-beans");
	}
	
	/**
	 * server moudle路径
	 * @return
	 */
	public static File serverPath() {
		ApplicationConfig config = GeneratorContext.applicationConfig;
		String projectName =  config.getProperty(PropKey.PROJECT_NAME.propKey());
		return new File(projectPath(),projectName+"-server");
	}
	
	/**
	 * 包名转路径
	 * @param packageName
	 * @return
	 */
	public static String package2FilePath(String packageName) {
		return packageName.replaceAll("\\.", "/");
	}
	
	/**
	 * 包名转file文件对象
	 * @param parantPath
	 * @param packageName
	 * @return
	 */
	public static File package2FilePath(String parantPath,String packageName) {
		return new File(parantPath,package2FilePath(packageName));
	}
}
