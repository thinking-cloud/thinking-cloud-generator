package tcg.core.file.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import tcg.core.bean.ColumnFieldMapping;
import tcg.core.bean.TableClassMapping;
import tcg.core.contant.MapperId;
import tcg.core.exception.GeneratorException;
import tcg.core.utils.FilePathUtils;
import tcg.core.utils.PackageNameUtils;

/**
 * 输出mapper xml文件
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class OutputMapperXml {

	public OutputMapperXml(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		File mapperXmlDir = FilePathUtils.package2FilePath(outputFilePath, PackageNameUtils.mapperXmlPackage());
		boolean mkdirs = mapperXmlDir.mkdirs();
		System.out.println("创建目录："+mapperXmlDir.getAbsolutePath()+" "+(mkdirs?"成功":"失败") );
		
		this.outputFilePath = mapperXmlDir.getAbsolutePath();
		this.tableClassMapping = tableClassMapping;
	}

	public static final String NEW_LINE = "\r";
	public static final String INDENT = "\t";

	private String outputFilePath;
	private List<TableClassMapping> tableClassMapping;
	
	

	public void generatorXml() {
		
		SAXReader mapperXml = new SAXReader();
		
		try {
			for (TableClassMapping table : tableClassMapping) {
				// mapper rootElement
				File mapperXmlFile = new File(outputFilePath,table.getMapperName()+".xml");
				System.out.println("正在写出:" + mapperXmlFile.getAbsolutePath());
				Document document = createMapper(mapperXml, mapperXmlFile.getAbsolutePath());
				// 添加明明空间
				docType(document);

				// result element
				Element mapper = document.getRootElement();
				String fullBeanName =table.getFullEntityName();
				addResultMap(mapper, fullBeanName, table.getColFieldMappingList());
				mapper.addText(NEW_LINE);

				// sql
				addSqlColumnsAll(mapper, table.getColFieldMappingList());
				mapper.addText(NEW_LINE);
				addSqlColumns(mapper, table.getColFieldMappingList());
				mapper.addText(NEW_LINE);
				addSqlValues(mapper, table.getColFieldMappingList());
				mapper.addText(NEW_LINE);
				addUpdateSet(mapper, table.getColFieldMappingList());
				mapper.addText(NEW_LINE);
				addReadWhere(mapper, table.getColFieldMappingList());
				mapper.addText(NEW_LINE);
				addWriteWhere(mapper, table.getColFieldMappingList());
				mapper.addText(NEW_LINE);

				// crud
				addInsert(mapper, table);
				mapper.addText(NEW_LINE);
				addDelete(mapper, table);
				mapper.addText(NEW_LINE);
				addUpdate(mapper, table);
				mapper.addText(NEW_LINE);
				addSelect(mapper, table);
				mapper.addText(NEW_LINE);
				addQueryPage(mapper, table);
				// 保存
				save(document, mapperXmlFile.getAbsolutePath());

			}

		} catch (Exception e) {
			throw new GeneratorException(e);
		}
	}

	/**
	 * 创建mapper根节点
	 * 
	 * @param document
	 * @param mapperName
	 */
	private Document createMapper(SAXReader _mapperXml, String mapperName) {
		// 创建document xml文档
		DocumentFactory documentFactory = _mapperXml.getDocumentFactory();
		Document root = documentFactory.createDocument();
		// 创建element mapper节点
		Element mapper = DocumentHelper.createElement("mapper").addAttribute("namespace", mapperName);
		root.add(mapper);
		return root;
	}

	/**
	 * 创建 resultMap
	 * 
	 * @param mapper
	 * @param entityName
	 */
	private Element addResultMap(Element mapper, String beanName, List<ColumnFieldMapping> columnList) {
		Element resultMap = DocumentHelper.createElement("resultMap");
		resultMap.addAttribute("id", MapperId.XML_RESULT_MAP).addAttribute("type", beanName);
		addResultMapProperty(resultMap, columnList);
		mapper.add(resultMap);
		return resultMap;
	}

	/**
	 * 在resultMap中添加子element
	 * 
	 * @param resultMap
	 * @param columnList
	 */
	private void addResultMapProperty(Element resultMap, List<ColumnFieldMapping> columnList) {
		for (ColumnFieldMapping col : columnList) {

			Element element = null;
			if (col.getColumnName().equalsIgnoreCase("id")) {
				// id
				element = DocumentHelper.createElement("id");
			} else if (!col.isFk()) {
				// 正常属性
				element = DocumentHelper.createElement("restult");
			} else {
				// 外键关联查询对象
				element = DocumentHelper.createElement("association");
				element.addAttribute("select", col.getFkMapper());
			}
			element.addAttribute("column", col.getColumnName());
			element.addAttribute("property", col.getFieldName());
			element.addAttribute("javaType", col.getFullFieldType());
			resultMap.add(element);
		}
	}

	/**
	 * 在mapper中添加列名
	 * 
	 * @param mapper
	 * @param columnList
	 */
	public void addSqlColumnsAll(Element mapper, List<ColumnFieldMapping> columnList) {
		Element sql = createSqlFormat(mapper, MapperId.XML_SQL_COLUMNS_ALL, ",");
		StringBuilder cols = new StringBuilder();
		for (ColumnFieldMapping columnFieldMapping : columnList) {
			cols.append(columnFieldMapping.getColumnName()).append(", ");
		}
		sql.addText(lineIdent(1, 3) + cols.toString() + lineIdent(1, 2));
	}

	/**
	 * 在mapper添加列名 (带条件)
	 * 
	 * @param mapper
	 * @param columnList
	 */
	private void addSqlColumns(Element mapper, List<ColumnFieldMapping> columnList) {
		Element sql = createSqlFormat(mapper, MapperId.XML_SQL_COLUMNS, ",");
		Element ifEle = null;
		for (ColumnFieldMapping col : columnList) {
			ifEle = DocumentHelper.createElement("if");
			ifEle.add(testAttribute(ifEle,col));
			ifEle.addText(lineIdent(1, 4) + col.getColumnName() + ", " + lineIdent(1, 3));
			sql.add(ifEle);
		}
	}

	/**
	 * 添加values
	 * 
	 * @param mapper
	 * @param columnList
	 */
	private void addSqlValues(Element mapper, List<ColumnFieldMapping> columnList) {
		Element sql = createSqlFormat(mapper, MapperId.XML_SQL_VALUES, ",");
		Element ifEle = null;
		for (ColumnFieldMapping col : columnList) {
			ifEle = DocumentHelper.createElement("if");
			ifEle.add(testAttribute(ifEle, col));
			ifEle.addText(lineIdent(1, 4) + fieldName(col) + "," + lineIdent(1, 3));
			sql.add(ifEle);
		}
	}

	/**
	 * 添加 update_set
	 * 
	 * @param mapper
	 * @param columnList
	 */
	private void addUpdateSet(Element mapper, List<ColumnFieldMapping> columnList) {
		Element sql = createSqlFormat(mapper, MapperId.XML_SQL_UPDATE_SET, ",");
		Element ifEle = null;
		for (ColumnFieldMapping col : columnList) {
			if(col.getColumnName().equals("create_user_id") || col.getColumnName().equals("create_time")) {
				continue;
			}
	 
			ifEle = DocumentHelper.createElement("if");
			ifEle.add(testAttribute(ifEle, col));
			ifEle.addText(lineIdent(1, 4) + col.getColumnName() + " = " + fieldName(col) + "," + lineIdent(1, 3));
			sql.add(ifEle);
			
		}
	}

	/**
	 * 添加 读取数据的查询条件
	 */
	private void addReadWhere(Element mapper, List<ColumnFieldMapping> columnList) {
		Element sql = createSqlFormat(mapper, MapperId.XML_SQL_READ_WHERE, "AND");
		Element ifEle = null;
		for (ColumnFieldMapping col : columnList) {
			ifEle = DocumentHelper.createElement("if");
			ifEle.add(testAttribute(ifEle, col));
			ifEle.addText(
					lineIdent(1, 4) + col.getColumnName() + " = "  + fieldName(col) +  " AND" + lineIdent(1, 3));
			sql.add(ifEle);
		}
	}

	/**
	 * 添加 写入数据的条件
	 */
	private void addWriteWhere(Element mapper, List<ColumnFieldMapping> columnList) {
		Element sql = createSqlFormat(mapper, MapperId.XML_SQL_WRITE_WHERE, "AND");
		Element ifEle = DocumentHelper.createElement("if");
		for (ColumnFieldMapping col : columnList) {
			if(col.getColumnName().equals("id") || col.getColumnName().equals("create_user_id")) {
				ifEle = DocumentHelper.createElement("if");
				ifEle.add(testAttribute(ifEle, col));
				ifEle.addText(
						lineIdent(1, 4) + col.getColumnName() + " = "  + fieldName(col) +  " AND" + lineIdent(1, 3));
				sql.add(ifEle);
			}
		}
	}

	/**
	 * 添加insert 模块
	 * <p>
	 * 	<insert useGeneratedKeys="true" keyProperty="id" parameterType=""> 
	 * 		INSERT	INTO table_name ( 
	 * 			<include refid="columns"/> 
	 * 		)VALUES(
	 * 			<include refid="values"/> ) 
	 *	</insert>
	 * </p>
	 * 
	 * @param mapper
	 * @param table
	 */
	private void addInsert(Element mapper, TableClassMapping table) {
		// 生成insert标签
		Element insert = DocumentHelper.createElement("insert");
		insert.addAttribute("id", MapperId.MAPPER_INSERT);
		insert.addAttribute("useGeneratedKeys", "true");
		insert.addAttribute("keyProperty", "id");
		insert.addAttribute("parameterType",table.getFullEntityName());

		// 生成inclue标签
		Element columnInclude = DocumentHelper.createElement("include");
		columnInclude.addAttribute("refid", MapperId.XML_SQL_COLUMNS);
		Element valuesInclude = DocumentHelper.createElement("include");
		valuesInclude.addAttribute("refid", MapperId.XML_SQL_VALUES);

		// 生成sql语句
		insert.addText(lineIdent(1, 2) + "INSERT INTO " + table.getTableName() + " ( " + lineIdent(0, 3));
		insert.add(columnInclude);
		insert.addText(lineIdent(1, 2) + ") VALUES ( " + lineIdent(0, 3));
		insert.add(valuesInclude);
		insert.addText(lineIdent(1, 2) + ")");

		// 将insert模块添加到 mapper中
		mapper.add(insert);
	}

	/**
	 * 添加 delete模块
	 * <p>
	 * 	<update id="delete" parameterType="">
	 * 		DELETE FROM table_name WHERE
	 * 		<include refid="write_where" />
	 * 	</update>
	 * </p>
	 * 
	 * @param mapper
	 * @param table
	 */
	private void addDelete(Element mapper, TableClassMapping table) {
		// 生成 delete标签
		Element delete = DocumentHelper.createElement("delete");
		delete.addAttribute("id", MapperId.MAPPER_DELETE);
		delete.addAttribute("parameterType",table.getFullEntityName());

		// 生成include标签
		Element writeWhereInclude = DocumentHelper.createElement("include");
		writeWhereInclude.addAttribute("refid", MapperId.XML_SQL_WRITE_WHERE);

		// 生成sql
		delete.addText(lineIdent(1, 2) + "DELETE FROM " + table.getTableName());
		delete.addText(lineIdent(1, 2) + "WHERE ");
		delete.add(writeWhereInclude);

		// 将update模块添加dao mapper模块
		mapper.add(delete);
	}

	/**
	 * update模块
	 * <p>
	 * 	<update id="update" parameterType="">
	 * 		UPDATE table_name SET
	 * 			<include refid="update_set" /> 
	 * 		WHERE 
	 * 			<include refid="w_where" />
	 *  </update>
	 * </p>
	 * @param mapper
	 * @param table
	 */
	private void addUpdate(Element mapper, TableClassMapping table) {
		// 生成 update标签
		Element update = DocumentHelper.createElement("update");
		update.addAttribute("id", MapperId.MAPPER_UPDATE);
		update.addAttribute("parameterType",table.getFullEntityName());

		// 生成include标签
		Element updateSetWhereInclude = DocumentHelper.createElement("include");
		updateSetWhereInclude.addAttribute("refid", MapperId.XML_SQL_UPDATE_SET);
		Element writeWhereInclude = DocumentHelper.createElement("include");
		writeWhereInclude.addAttribute("refid", MapperId.XML_SQL_WRITE_WHERE);

		// 生成sql
		update.addText(lineIdent(1, 2) + "UPDATE " + table.getTableName() + " SET ");
		update.add(updateSetWhereInclude);
		update.addText(lineIdent(1, 2) + "WHERE");
		update.add(writeWhereInclude);

		// 将update模块添加dao mapper模块
		mapper.add(update);
	}

	/**
	 * 添加select模块 
	 * <p>
	 * 	<select id="select" resultMap="" parameterType="java.lang.Long">
	 * 		SELECT 
	 * 			<include refid="columns_all" /> 
	 * 		FROM 
	 * 			assets_manage_code_repository
	 * 		WHERE 
	 * 			id = #{id} 
	 *	</select>
	 * </p>
	 * @param mapper
	 * @param table
	 */
	private void addSelect(Element mapper, TableClassMapping table) {
		// 生成 select标签
		Element update = DocumentHelper.createElement("select");
		update.addAttribute("id", MapperId.MAPPER_SELECT);
		update.addAttribute("resultMap", MapperId.XML_RESULT_MAP);
		update.addAttribute("parameterType", table.getPk().getFieldType());

		// 生成include标签
		Element columnsInclude = DocumentHelper.createElement("include").addAttribute("refid",
				MapperId.XML_SQL_COLUMNS_ALL);
		// Element readerWhereInclude =
		// DocumentHelper.createElement("include").addAttribute("refid",
		// MapperId.XML_SQL_READ_WHERE);

		// 生成sql
		update.addText(lineIdent(1, 2) + "SELECT ");
		update.add(columnsInclude);
		update.addText(lineIdent(1, 2) + "FROM " + table.getTableName());
		update.addText(lineIdent(1, 2) + "WHERE id = #{id}");

		// 将update模块添加dao mapper模块
		mapper.add(update);
	}

	/**
	 * 分页查询
	 * <p>
	 *	<select id="queryPage" resultMap="" parameterType="">
	 * 		SELECT 
	 * 			<include refid="columns_all" />
	 * 		FROM
	 * 			assets_manage_code_repository 
	 * 		<where>
	 * 			<include refid="r_where" />
	 * 		</where>
	 * 	</select>
	 * </p> 
	 * @param mapper
	 * @param table
	 */
	private void addQueryPage(Element mapper, TableClassMapping table) {
		// 生成 select标签
		Element update = DocumentHelper.createElement("select");
		update.addAttribute("id", MapperId.MAPPER_PAGE);
		update.addAttribute("resultMap", MapperId.XML_RESULT_MAP);
		update.addAttribute("parameterType", table.getPk().getFullFieldType());

		// 生成include标签
		Element columnsInclude = DocumentHelper.createElement("include").addAttribute("refid",
				MapperId.XML_SQL_COLUMNS_ALL);
		Element readerWhereInclude = DocumentHelper.createElement("include").addAttribute("refid",
				MapperId.XML_SQL_READ_WHERE);
		Element where = DocumentHelper.createElement("where");

		// 生成sql
		update.addText(lineIdent(1, 2) + "SELECT ");
		update.add(columnsInclude);
		update.addText(lineIdent(1, 2) + "FROM " + table.getTableName());
		update.add(where);
		where.add(readerWhereInclude);

		// 将update模块添加dao mapper模块
		mapper.add(update);
	}

	// <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
	// "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
	private void docType(Document document) {
		document.addDocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN",
				"http://mybatis.org/dtd/mybatis-3-mapper.dtd");
	}

	/**
	 * 保存xml
	 * 
	 * @param document
	 * @param mapperXmlWriter
	 */
	private void save(Document document, String outputFilePath) {
		try (BufferedWriter mapperXmlWriter = new BufferedWriter(new FileWriter(outputFilePath))) {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			format.setIndent(true); // 设置是否缩进
			format.setIndent("\t"); // 以空格方式实现缩进
			format.setTrimText(false);
			XMLWriter xw = null;
			try {
				xw = new XMLWriter(mapperXmlWriter, format);
				xw.write(document);
			} finally {
				if (xw != null) {
					xw.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建sql element
	 * 
	 * @param id
	 * @param suffixOverrides
	 * @return
	 */
	private Element createSqlFormat(Element mapper, String id, String suffixOverrides) {
		Element sql = DocumentHelper.createElement("sql").addAttribute("id", id);
		mapper.add(sql);
		boolean trimFlag = false;
		Element trim = DocumentHelper.createElement("trim");

		if (trimFlag = suffixOverrides != null) {
			trim.addAttribute("suffixOverrides", suffixOverrides);
		}

		if (trimFlag) {
			sql.add(trim);
			return trim;
		}
		return sql;
	}
	
	/**
	 * 生成test属性
	 * @param ifEle
	 * @param col
	 * @return
	 */
	private Attribute testAttribute(Element ifEle, ColumnFieldMapping col) {
		if(col.isFk()) {
			return DocumentHelper.createAttribute(ifEle, "test",  col.getFieldName() + " != null and "+col.getFieldName() +".id != null");
		}else {
			return DocumentHelper.createAttribute(ifEle, "test",  col.getFieldName() + " != null ");
		}
	}
	
	/**
	 * 生成获取实体属性值的表达表达式 #{value}
	 * @param col
	 * @return
	 */
	private String fieldName(ColumnFieldMapping col) {
		if(col.isFk()) {
			return " #{" + col.getFieldName() + ".id}";
		}else {
			return " #{" + col.getFieldName() + "}";
		}
	}

	/**
	 * 生成text起始格式 / 结束格式
	 * 
	 * @param startNewlineSize 换行符长度
	 * @param identSize        缩进符长度
	 * @return
	 */
	private String lineIdent(int startNewlineSize, int identSize) {
		StringBuilder start = new StringBuilder();
		for (int i = 0; i < startNewlineSize; i++) {
			start.append(NEW_LINE);
		}
		for (int i = 0; i < identSize; i++) {
			start.append(INDENT);
		}
		return start.toString();
	}
}
