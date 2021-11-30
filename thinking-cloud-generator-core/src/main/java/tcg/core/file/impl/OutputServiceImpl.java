package tcg.core.file.impl;

import java.io.PrintStream;
import java.util.List;

import tcg.core.bean.TableClassMapping;
import tcg.core.file.OutputJavaFile;
import tcg.core.utils.PackageNameUtils;

/**
 * 输出 serviceImpl文件
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class OutputServiceImpl extends OutputJavaFile {

	public OutputServiceImpl(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		super(outputFilePath, tableClassMapping);
	}

	@Override
	public String getFileName(TableClassMapping table) {
		return table.getServiceImplName();
	}

	@Override
	protected String packageName()  {
		return PackageNameUtils.serviceImplPackage();
	}

	@Override
	protected void outputAnnotation(TableClassMapping table, PrintStream ps) {
		ps.println("@Service");
	}

	@Override
	protected void outputClassName(TableClassMapping table, PrintStream ps) {
		StringBuilder line = new StringBuilder();
		line.append("public ");
		line.append("class ");
		line.append(table.getServiceImplName()+" ") ;
		line.append("implements ");
		line.append(table.getServiceName());
		line.append("{\n");
		ps.println(line.toString());	
	}

	@Override
	protected void outputClassFiled(TableClassMapping table, PrintStream ps) {
		
	}

	@Override
	protected void outputMethod(TableClassMapping table, PrintStream ps) {
		ps.println(methodIndent + "@Override");
		ps.println(methodIndent + "public Mapper<"+table.getEntityName()+", "+table.getPk().getFieldType()+"> getBaseMapper() {");
		ps.println(methodBodyIndent+"return " + table.getMapperName()+";" );
		ps.println(methodIndent+"}");
	}
	
}
