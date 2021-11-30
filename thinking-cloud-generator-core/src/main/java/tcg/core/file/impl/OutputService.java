package tcg.core.file.impl;

import java.io.PrintStream;
import java.util.List;

import tcg.core.bean.TableClassMapping;
import tcg.core.file.OutputJavaFile;
import tcg.core.utils.PackageNameUtils;

/**
 * 生成service 接口
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class OutputService extends OutputJavaFile {

	public OutputService(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		super(outputFilePath, tableClassMapping);
	}

	@Override
	protected String packageName()  {
		return  PackageNameUtils.servicePackage();
	}

	@Override
	protected void outputAnnotation(TableClassMapping table, PrintStream ps) {	
	}
	

	@Override
	protected void outputClassName(TableClassMapping table, PrintStream ps) {
		String generic = "<"+table.getEntityName()+", "+table.getPk().getFieldType()+">";
		StringBuilder line = new StringBuilder();
		line.append("public ");
		line.append("interface ");
		line.append(table.getServiceName()+" ") ;
		line.append("extends ");
		line.append("DService");
		line.append(generic);
		line.append("{\n");
		ps.println(line.toString());	
	}

	@Override
	protected void outputClassFiled(TableClassMapping table, PrintStream ps) {
		
	}

	@Override
	public String getFileName(TableClassMapping table) {
		return table.getServiceName();
	}

	@Override
	protected void outputMethod(TableClassMapping table, PrintStream ps) {
		// TODO Auto-generated method stub
		
	}

}
