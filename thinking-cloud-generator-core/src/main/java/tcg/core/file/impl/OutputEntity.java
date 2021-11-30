package tcg.core.file.impl;

import java.io.PrintStream;
import java.util.List;

import tcg.core.bean.ColumnFieldMapping;
import tcg.core.bean.TableClassMapping;
import tcg.core.file.OutputJavaFile;
import tcg.core.utils.PackageNameUtils;

/**
 * 生成java文件
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class OutputEntity extends OutputJavaFile {

	public OutputEntity(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		super(outputFilePath, tableClassMapping);
	}

	@Override
	public String getFileName(TableClassMapping table) {
		return table.getEntityName();
	}

	@Override
	protected String packageName()  {
		return PackageNameUtils.entityPackage();
		
	}

	@Override
	protected void outputAnnotation(TableClassMapping table, PrintStream ps) {
		ps.println("@Data");
		ps.println("@EqualsAndHashCode(callSuper = false)");
		ps.println("@NoArgsConstructor");
		
	}

	@Override
	protected void outputClassName(TableClassMapping table, PrintStream ps) {
		ps.println("public class "+table.getEntityName()+" extends BaseEntity<"+table.getPk().getFieldType()+",String> {");
		
	}

	@Override
	protected void outputClassFiled(TableClassMapping table, PrintStream ps) {
		String ignoreFieldNames="id,createUserId,createTime,lastUpdateUserId,lastUpdateTime,";
		for (ColumnFieldMapping col : table.getColFieldMappingList()) {	
			if(ignoreFieldNames.indexOf(col.getFieldName()+",")==-1) {
				ps.println(methodIndent+"private "+col.getFieldType()+" "+col.getFieldName()+";");
			}
		}
	}
	
	

	@Override
	protected void outputImport(TableClassMapping table, PrintStream ps) {
		super.outputImport(table, ps);
		ps.println("import lombok.Data;");
		ps.println("import lombok.EqualsAndHashCode;");
		ps.println("import lombok.NoArgsConstructor;");
		ps.println("import thinking.cloud.beans.entity.abs.BaseEntity;");
	}

	@Override
	protected void outputMethod(TableClassMapping table, PrintStream ps) {
		
	}
}
