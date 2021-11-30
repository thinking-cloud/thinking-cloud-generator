package tcg.core.dir.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import lombok.AllArgsConstructor;
import tcg.core.dir.InitProject;
import tcg.core.exception.GeneratorException;
import tcg.core.utils.ExpressionReplaceUtils;
import tcg.core.utils.FilePathUtils;

/**
 * 初始化maven项目
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
@AllArgsConstructor
public class InitMavenProject extends InitProject {
		
	@Override
	public void createProjectDir() {
		mkdirs(FilePathUtils.projectPath());
		createMavenProject(FilePathUtils.apiPath());
		createMavenProject(FilePathUtils.beansPath());
		createMavenProject(FilePathUtils.serverPath());
	}

	@Override
	public void createConfig() {
		careateMavenPom();
		createSpringCloudConfig();
	}
		
	/**
	 * 创建spring cloud 项目的配置文件
	 */
	private void createSpringCloudConfig() {
		//bootstrap.yml
		String bootstrap = new File(FilePathUtils.serverPath(),"/bootstrap.yml").getAbsolutePath();
		outputConfigFile(bootstrap, "template/spring/bootstrap.yml");
		
		String application = new File(FilePathUtils.serverPath(),"/application.yml").getAbsolutePath();
		outputConfigFile(application, "template/spring/application.yml");
	}
	
	/**
	 * 创建mvane项目的pom.xml配置文件
	 */
	private void careateMavenPom() {
		// pom.xml
		String pomPath = new File(FilePathUtils.projectPath(),"/pom.xml").getAbsolutePath();
		outputConfigFile(pomPath, "template/pom/project-pom.xml");
		// api/pom.xml
		pomPath = new File(FilePathUtils.apiPath(),"/pom.xml").getAbsolutePath();
		outputConfigFile(pomPath, "template/pom/module-api-pom.xml");
		// beans/pom.xml
		pomPath = new File(FilePathUtils.beansPath(),"/pom.xml").getAbsolutePath();
		outputConfigFile(pomPath, "template/pom/module-beans-pom.xml");
		// server/pom.xml
		pomPath = new File(FilePathUtils.serverPath(),"/pom.xml").getAbsolutePath();
		outputConfigFile(pomPath, "template/pom/module-server-pom.xml");
		
	}
	
	/**
	 * 创建maven项目
	 * @param basePath
	 * @param projectName
	 */
	private void createMavenProject(File project) {
		mkdirs(project);
		File mainJava = new File(project, FilePathUtils.MAIN_JAVA_PATH);
		mkdirs(mainJava);
		File mainResource = new File(project, FilePathUtils.MAIN_RESOURCES_PATH);
		mkdirs(mainResource);
		File mainTest = new File(project, FilePathUtils.TEST_JAVA_PATH);
		mkdirs(mainTest);
		File mainResources = new File(project, FilePathUtils.TEST_RESOURCES_PATH);
		mkdirs(mainResources);
		File target = new File(project, FilePathUtils.TARGET_PATH);
		mkdirs(target);
	}
	
	/**
	 * 输出配置文件的处理方法
	 * @param pomPath
	 * @param templatePomPath
	 */
	private void outputConfigFile(String pomPath,String templatePomPath) {
		templatePomPath = InitMavenProject.class.getClassLoader().getResource(templatePomPath).getPath();
		try(
			BufferedReader templatePom = new BufferedReader(new InputStreamReader(new FileInputStream(templatePomPath),"UTF-8"))
		){
			
			try(PrintStream pom = new PrintStream(pomPath,"UTF-8")){
				String line = null;
				while((line = templatePom.readLine())!=null) {
					pom.println(ExpressionReplaceUtils.replaceAll(line));
				}
			}
		} catch (Exception e) {
			throw new GeneratorException(e);
		}finally {
			System.out.println("创建配置文件："+pomPath);
		}
	}
	
	/**
	 * 创建目录基本方式
	 * @param file 
	 */
	private void mkdirs(File file) {
		boolean flag = file.mkdirs();
		System.out.println("创建目录："+file.getAbsolutePath()+" - " + flag);
	}
}
