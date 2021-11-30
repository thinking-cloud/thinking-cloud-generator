package tcg.core.service;

import java.io.File;
import java.util.List;

import tcg.core.bean.TableClassMapping;
import tcg.core.db.ReaderMateData;
import tcg.core.dir.InitProject;
import tcg.core.dir.impl.InitMavenProject;
import tcg.core.file.OutputJavaFile;
import tcg.core.file.impl.OutputAdapter;
import tcg.core.file.impl.OutputApi;
import tcg.core.file.impl.OutputApiVO;
import tcg.core.file.impl.OutputBo;
import tcg.core.file.impl.OutputController;
import tcg.core.file.impl.OutputEntity;
import tcg.core.file.impl.OutputMapper;
import tcg.core.file.impl.OutputMapperXml;
import tcg.core.file.impl.OutputService;
import tcg.core.file.impl.OutputServiceImpl;
import tcg.core.file.impl.OutputVO;
import tcg.core.utils.ConnectionUtils;
import tcg.core.utils.FilePathUtils;

/**
 * 业务实现类
 * 
 * @author think
 * @Date 2021-11-30
 * @version 1.0.0
 */
public class GeneratorService {
	public void generatorService() {
		try (ConnectionUtils connectionUtils = new ConnectionUtils()) {
			ReaderMateData metaData = new ReaderMateData(connectionUtils.getMetaData());

			System.out.println("获取数据库元数据");
			List<TableClassMapping> tableMatedata = metaData.tableMatedata();
			for (TableClassMapping table : tableMatedata) {
				metaData.columnMateData(table);
			}
			InitProject initProject = new InitMavenProject();
			initProject.init();
			
			String beansModuleFilePath = new File(FilePathUtils.beansPath(),FilePathUtils.MAIN_JAVA_PATH).getAbsolutePath();
			String serverModuleFilePath = new File(FilePathUtils.serverPath(),FilePathUtils.MAIN_JAVA_PATH).getAbsolutePath();
			String apiModuleFilePath = new File(FilePathUtils.apiPath(),FilePathUtils.MAIN_JAVA_PATH).getAbsolutePath();
		    // beans
			OutputJavaFile oe = new OutputEntity(beansModuleFilePath, tableMatedata);
		    oe.outputJavaFile();
		    OutputJavaFile ob = new OutputBo(beansModuleFilePath, tableMatedata);
		    ob.outputJavaFile();
		    // vo
		    OutputJavaFile ov = new OutputVO(serverModuleFilePath, tableMatedata);
		    ov.outputJavaFile();
		    OutputJavaFile oav = new OutputApiVO(apiModuleFilePath, tableMatedata);
		    oav.outputJavaFile();
		    // api
		    OutputJavaFile oapi = new OutputApi(apiModuleFilePath, tableMatedata);
		    oapi.outputJavaFile();
		    // server
		    OutputMapperXml gmx = new OutputMapperXml(serverModuleFilePath, tableMatedata);
		    gmx.generatorXml();
		    OutputJavaFile om = new OutputMapper(serverModuleFilePath, tableMatedata);
		    om.outputJavaFile();
		    OutputJavaFile os = new OutputService(serverModuleFilePath, tableMatedata);
		    os.outputJavaFile();
		    OutputJavaFile osi = new OutputServiceImpl(serverModuleFilePath, tableMatedata);
		    osi.outputJavaFile();
		    OutputJavaFile oa = new OutputAdapter(serverModuleFilePath, tableMatedata);
		    oa.outputJavaFile();
		    OutputJavaFile oc = new OutputController(serverModuleFilePath, tableMatedata);
		    oc.outputJavaFile();

		    

		   

		}
	}
}