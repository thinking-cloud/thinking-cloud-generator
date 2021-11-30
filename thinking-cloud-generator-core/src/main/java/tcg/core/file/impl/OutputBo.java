package tcg.core.file.impl;

import java.io.PrintStream;
import java.util.List;

import tcg.core.bean.TableClassMapping;
import tcg.core.file.OutputJavaFile;
import tcg.core.utils.PackageNameUtils;

/**
 * 输出BO文件
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class OutputBo extends OutputJavaFile{

	public OutputBo(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		super(outputFilePath, tableClassMapping);
	}

	@Override
	public String getFileName(TableClassMapping table) {
		return table.getBoName();
	}

	@Override
	protected String packageName()  {
		return PackageNameUtils.boPackage();
		
	}

	@Override
	protected void outputAnnotation(TableClassMapping table, PrintStream ps) {
		ps.println("@Data");
	}

	@Override
	protected void outputClassName(TableClassMapping table, PrintStream ps) {
		ps.println("public class "+table.getBoName()+" extends "+table.getEntityName()+" {");
	}

	@Override
	protected void outputClassFiled(TableClassMapping table, PrintStream ps) {

	}

	@Override
	protected void outputMethod(TableClassMapping table, PrintStream ps) {
		
	}
	
}
