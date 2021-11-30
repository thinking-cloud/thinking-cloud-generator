package tcg.core.utils;

import tcg.core.config.GeneratorContext;
import tcg.core.enums.PropKey;

/**
 *  表达式替换
 *  
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class ExpressionReplaceUtils {
	/**
	 * 替换模板文件中的表达式
	 * @param line
	 * @return
	 */
	public static String replaceAll(String line) {
		for (PropKey templateExpression : PropKey.values()) {
			String express = "#{"+templateExpression.name()+"}";
			while(line.indexOf(express)!=-1) {
				line = line.replace(express, GeneratorContext.applicationConfig.getProperty(templateExpression.propKey()));
			}
		}
		return line;
	}
}
