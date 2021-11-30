package tcg.core.db;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import tcg.core.bean.ColumnFieldMapping;
import tcg.core.bean.TableClassMapping;
import tcg.core.config.ApplicationConfig;
import tcg.core.config.GeneratorContext;
import tcg.core.enums.PropKey;
import tcg.core.exception.GeneratorException;

/**
 * 读取数据库元数据
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class ReaderMateData {
    private DatabaseMetaData mateData;
    private ApplicationConfig config= GeneratorContext.applicationConfig;
    public ReaderMateData(DatabaseMetaData mateData) {
	this.mateData = mateData;
    }

    /**
     * 获取表的元数据
     */
    public List<TableClassMapping> tableMatedata() {

	List<TableClassMapping> tableList = new LinkedList<>();
	String databaseName = config.getProperty(PropKey.DATABASE_NAME.propKey());
	try (ResultSet resultSet = mateData.getTables(databaseName, null, null, null)) {
	    while (resultSet.next()) {
		String tableName = resultSet.getString("TABLE_NAME");
		// 排除未选中的表 进行处理
		String tableNamesStr = config.getProperty(PropKey.TABLE_NAME.propKey());
		b:if(tableNamesStr!=null && !tableNamesStr.trim().equals("")) {
		    for (String tn : tableNamesStr.split(",")) {
				if (tn.equalsIgnoreCase(tableName)) {
				    break b;
				}
			    continue;
			}
		}
		TableClassMapping table = new TableClassMapping(tableName);
		tableList.add(table);
	    }
	} catch (SQLException e) {
	    throw new GeneratorException("获取数据库中的表出现异常", e);
	}
	return tableList;
    }

    /**
     * 获取列的元数据
     */
    public TableClassMapping columnMateData(TableClassMapping table) {
	String dataBaseName = config.getProperty(PropKey.DATABASE_NAME.propKey());
	try (ResultSet resultSet = mateData.getColumns(dataBaseName, null, table.getTableName(), null)) {
	    while (resultSet.next()) {
		String columnName = resultSet.getString("COLUMN_NAME");
		String columnType = resultSet.getString("TYPE_NAME");
		ColumnFieldMapping columnFieldMapping = new ColumnFieldMapping(columnType, columnName,
			table.getImportPackageSet());
		if (columnName.equalsIgnoreCase("id")) {
		    table.setPk(columnFieldMapping);
		}
		table.getColFieldMappingList().add(columnFieldMapping);
	    }
	    return table;
	} catch (Exception e) {
	    throw new GeneratorException("获取数据库中的列出现异常", e);
	}
    }
}
