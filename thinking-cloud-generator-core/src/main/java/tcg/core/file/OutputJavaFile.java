package tcg.core.file;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import tcf.utils.date.DateFormatUtils;
import tcg.core.bean.TableClassMapping;
import tcg.core.exception.GeneratorException;
import tcg.core.utils.FilePathUtils;

/**
 * 输出java代码文件
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public abstract class OutputJavaFile {

	protected final static String methodIndent = "\t";
	protected final static String methodBodyIndent = "\t\t";
	
    private String outputFilePath;
    private List<TableClassMapping> tableClassMapping;
    
	public OutputJavaFile(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		File javaFilDir = FilePathUtils.package2FilePath(outputFilePath, packageName());
		boolean mkdirs = javaFilDir.mkdirs();
		System.out.println("创建目录："+javaFilDir.getAbsolutePath()+" "+(mkdirs?"成功":"失败") );
		
		this.outputFilePath = javaFilDir.getAbsolutePath();
		this.tableClassMapping = tableClassMapping;
	}
    
    /**
     * 输出文件
     */
    public void outputJavaFile() {
	for (TableClassMapping table : tableClassMapping) {
	    String path = outputFilePath + "/" + getFileName(table) + ".java";
	    System.out.print("正在写出:"+path);
	    try (PrintStream ps = new PrintStream(new File(path))) {
		System.out.println();
    	ps.println("package "+packageName()+";");
		outputImport(table, ps);
		outputClassComment(table, ps);
		outputAnnotation(table, ps);
		outputClassName(table, ps);
		outputClassFiled(table, ps);
		ps.println();
		outputMethod(table, ps);
		ps.println("}");
	    } catch (Exception e) {
		throw new GeneratorException(e);
	    }
	}
    }

    public abstract String getFileName(TableClassMapping table);

    /**
     * 输出包名
     * 
     * @param table
     * @param ps
     */
    protected abstract String packageName();

    /**
     * 输出引入的包
     * 
     * @param table
     * @param ps
     */
    protected void outputImport(TableClassMapping table, PrintStream ps) {
		Set<String> importPackageSet = table.getImportPackageSet();
		for (String importPackage : importPackageSet) {
		    ps.println("import " + importPackage + ";");
		}
    }

    /**
     * 输出注释
     * 
     * @param table
     * @param ps
     */
    protected void outputClassComment(TableClassMapping table, PrintStream ps) {
	ps.println();
	ps.println("/**");
	ps.println(" * thinking cloud 自动生成");
	ps.println(" * <p></p>");
	ps.println(" * @author thinking");
	ps.println(" * @author " + DateFormatUtils.toString(new Date()));
	ps.println(" */");
    }

    /**
     * 输出类上注解
     * 
     * @param table
     * @param ps
     */
    protected abstract void outputAnnotation(TableClassMapping table, PrintStream ps);

    /**
     * 输出类名及父类接口
     * 
     * @param table
     * @param ps
     */
    protected abstract void outputClassName(TableClassMapping table, PrintStream ps);

    /**
     * 输出属性
     * 
     * @param colFieldMappingList
     * @param ps
     */
    protected abstract void outputClassFiled(TableClassMapping table, PrintStream ps);

    
    /**
     * 输出业务方法
     * @param colFieldMappingList
     * @param ps 
     */
    protected abstract void outputMethod(TableClassMapping table, PrintStream ps);
    
}
