package tcg.core.file.impl;

import java.io.PrintStream;
import java.util.List;

import tcg.core.bean.ColumnFieldMapping;
import tcg.core.bean.TableClassMapping;
import tcg.core.file.OutputJavaFile;
import tcg.core.utils.PackageNameUtils;

/**
 * 生成API VO文件
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class OutputApiVO extends OutputJavaFile {

	public OutputApiVO(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		super(outputFilePath, tableClassMapping);
	}

	@Override
	public String getFileName(TableClassMapping table) {
		return table.getApiName();
	}

	@Override
	protected String packageName()  {
		return PackageNameUtils.apiVoPackage();
	}

	@Override
	protected void outputAnnotation(TableClassMapping table, PrintStream ps) {
		ps.println("@Data");
		ps.println("@EqualsAndHashCode(callSuper = false)");
	}

	@Override
	protected void outputClassName(TableClassMapping table, PrintStream ps) {
		ps.println("public class "+table.getApiVoName()+" extends VO<"+table.getEntityName()+">{");
	}

	@Override
	protected void outputClassFiled(TableClassMapping table, PrintStream ps) {
		for (ColumnFieldMapping col : table.getColFieldMappingList()) {
			ps.println(methodIndent+"private "+col.getFieldType()+" "+col.getFieldName()+";");
		}
	}

	@Override
	protected void outputMethod(TableClassMapping table, PrintStream ps) {
		
	}

}
