package tcg.core;

import tcg.core.service.GeneratorService;

/**
 * 启动类
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class GeneratorStart {
	
	public static void main(String[] args) {
		try {
			GeneratorService generatorService = new GeneratorService();
			generatorService.generatorService();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
