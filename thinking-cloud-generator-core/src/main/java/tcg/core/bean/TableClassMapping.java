package tcg.core.bean;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lombok.Data;
import tcf.utils.reflect.ReflectBeanUtils;
import tcg.core.config.ApplicationConfig;
import tcg.core.config.GeneratorContext;
import tcg.core.utils.PackageNameUtils;

/**
 * 表与类的对应关系
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
@Data
public class TableClassMapping {
	private String tableName;
	private ColumnFieldMapping pk;
	private String entityName;
	private String fullEntityName;
	private String boName;
	private String fullBoName;
	private String limitBoName;
	private String fullLimitBoBName;
	private String apiVoName;
	private String fullApiVoName;
	private String voName;
	private String fullVoName;
	private String apiName;
	private String fullApiName;
	private String controllerName;
	private String fullControllerName;
	private String serviceName;
	private String fullServiceName;
	private String serviceImplName;
	private String fullServiceImplName;
	private String mapperName;
	private String fullMapperName;
	private String adapterName;
	private String fullAdapterName;
	private List<ColumnFieldMapping> colFieldMappingList=new LinkedList<>();
	private Set<String> importPackageSet = new LinkedHashSet<>();
	private String comment;
	
	/**
	 * 
	 * @param tableName 表名
	 * @param pkClass 主键类型
	 */
	public TableClassMapping(String tableName) {
		ApplicationConfig config = GeneratorContext.applicationConfig;
		this.tableName = tableName;
		this.entityName = ReflectBeanUtils.snakeCase2CamelCase(false, tableName);
		this.fullEntityName = PackageNameUtils.entityPackage()+"."+entityName;
		this.boName = entityName+"Bo";
		this.fullBoName=PackageNameUtils.boPackage()+"."+boName;
		this.limitBoName = entityName+"LimitBo";
		this.fullLimitBoBName=PackageNameUtils.boPackage()+"."+limitBoName;
		this.apiVoName = entityName+"ApiVo";
		this.fullVoName = PackageNameUtils.boPackage()+"."+apiVoName;
		this.voName = entityName+"Vo";
		this.fullVoName = PackageNameUtils.voPackage()+"."+voName;
		this.apiName = entityName+"Api";
		this.fullApiName = PackageNameUtils.apiPackage()+"."+apiName;
		this.controllerName = entityName+"Controller";
		this.fullControllerName = PackageNameUtils.controllerPackage()+"."+apiName;
		this.serviceName = entityName+"Service";
		this.fullServiceName = PackageNameUtils.servicePackage()+"."+serviceName;
		this.serviceImplName = entityName+"ServiceImpl";
		this.fullServiceImplName = PackageNameUtils.serviceImplPackage()+"."+serviceImplName;
		this.mapperName = entityName+"Mapper";
		this.fullMapperName = PackageNameUtils.mapperPackage()+"."+mapperName;
		this.adapterName = entityName+"Adapter";
		this.fullAdapterName = PackageNameUtils.adapterPackage()+"."+adapterName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableClassMapping other = (TableClassMapping) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}
	
	
}
