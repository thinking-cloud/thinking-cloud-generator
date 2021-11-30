package tcg.core.enums;

/**
 * 记录配置文件中key的枚举
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public enum PropKey {
	PROJECT_NAME(0),
	PROJECT_VERSION(1),
	PROJECT_PATH(2),
	BASE_PACKAGE(3),
	PROJECT_DESCRIPTION(4),
	PROJECT_AUTHOR(5),
	PROJECT_AUTHOR_EMAIL(6),
	
	RA_SERVER_HOST(10),
	RA_SERVER_PORT(11),
	RA_SERVER_TOKEN(12),
	SERVER_PORT(13),
	
	JDBC_ADDRESS(20),
	JDBC_USERNAME(21), 
	JDBC_PASSWORD(22), 
	DATABASE_NAME(23), 
	TABLE_NAME(24),
	CLASS_NAME_PREFIX(25);

	String propKey;

	PropKey(int index) {
		if(index<10) {
			propKey = projectPropKeys[index];
		}else if(index - 10 <10){
			propKey = raPropKeys[index-10];
		}else if(index-20 < 10) {
			propKey = jdbcPropKeys[index-20];
		}
		
	}

	public String propKey() {
		return propKey;
	}
	
	String[] projectPropKeys = { 
			"project-name",
			"project-version",
			"project-path",
			"base-package",
			"project-description",
			"project-author",
			"project-author_email"
			};
	String[] raPropKeys = { 
			"ra-server-host",
			"ra-server-port",
			"ra-server-token",
			"server-port"
			};
	String[] jdbcPropKeys = {
			"jdbc-address", 
			"jdbc-username", 
			"jdbc-password", 
			"database-name",
			"table-name", 
			"class-name-prefix" 
			};

}
