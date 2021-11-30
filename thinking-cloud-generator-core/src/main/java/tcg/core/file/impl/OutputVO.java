package tcg.core.file.impl;

import java.io.PrintStream;
import java.util.List;

import tcf.utils.reflect.ReflectBeanUtils;
import tcg.core.bean.ColumnFieldMapping;
import tcg.core.bean.TableClassMapping;
import tcg.core.file.OutputJavaFile;
import tcg.core.utils.PackageNameUtils;

/**
 * 输出VO类
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class OutputVO extends OutputJavaFile {

	public OutputVO(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		super(outputFilePath, tableClassMapping);
	}

	@Override
	public String getFileName(TableClassMapping table) {
		return table.getVoName();
	}

	@Override
	protected String packageName()  {
		return PackageNameUtils.voPackage();
		
	}

	@Override
	protected void outputAnnotation(TableClassMapping table, PrintStream ps) {
		
	}

	@Override
	protected void outputClassName(TableClassMapping table, PrintStream ps) {
		ps.println("public class "+table.getVoName()+" extends "+table.getApiVoName()+"{");
	}

	@Override
	protected void outputClassFiled(TableClassMapping table, PrintStream ps) {
		
	}

	@Override
	protected void outputMethod(TableClassMapping table, PrintStream ps) {
		for (ColumnFieldMapping col : table.getColFieldMappingList()) {
			ps.println(methodIndent+"@Override");
			String methodName = ReflectBeanUtils.fieldName2MethodName("get", col.getFieldName());
			ps.println(methodIndent+"public "+col.getFieldType()+" "+methodName+"(){");
			ps.println(methodBodyIndent+"return source."+methodName+"();");
			ps.println(methodIndent+")");
		}
	}
}
