package tcg.core.dir;

/**
 * 初始化项目的接口
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public abstract class InitProject {
	
	/**
	 * 初始化核心方法
	 */
	public void init() {
		createProjectDir();
		createConfig();
	}
	
	/**
	 * 创建项目目录
	 */
	public abstract void createProjectDir();
	
	/**
	 * 创建配置文件
	 */
	public abstract void createConfig();
	
}
