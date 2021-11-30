package tcg.core.exception;

/**
 * 代码生成业务逻辑的异常
 * 在当前模块中，所有异常都应该是此异常的子类
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class GeneratorException extends RuntimeException {

	public GeneratorException() {
	}

	public GeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneratorException(String message) {
		super(message);
	}

	public GeneratorException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -1101655112599303400L;
	
}
