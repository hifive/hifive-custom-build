/*
 * Copyright (C) 2012-2016 NS Solutions Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.htmlhifive.sourcebuilder.ant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.javascript.jscomp.SourceMap;
import com.htmlhifive.sourcebuilder.common.SourceBuilderConstants;
import com.htmlhifive.sourcebuilder.model.OutputSource;
import com.htmlhifive.sourcebuilder.service.AntVersionService;
import com.htmlhifive.sourcebuilder.service.BuildNameService;
import com.htmlhifive.sourcebuilder.service.BuilderService;
import com.htmlhifive.sourcebuilder.service.ContentCleanService;
import com.htmlhifive.sourcebuilder.service.ModuleService;
import com.htmlhifive.sourcebuilder.service.VelocityBuildService;

public class BuildRunner {

	private static Logger logger = LoggerFactory.getLogger(BuildRunner.class);

	@Inject
	private ModuleService moduleService;
	@Inject
	private BuilderService builderService;
	@Inject
	private BuildNameService buildNameService;
	@Inject
	private ContentCleanService contentCleanService;
	@Inject
	private AntVersionService antVersionService;
	@Inject
	private VelocityBuildService velocityBuildService;

	public void execute(BuildParameter param) {

		logger.info(param.toString());
		try {
			if (StringUtils.isNotEmpty(param.getVersion())) {
				antVersionService.setCurrentVersion(param.getVersion());
			}
			if (StringUtils.isNotEmpty(param.getTemplateEngineVersion())) {
				antVersionService.setTemplateEngineVersion(param.getTemplateEngineVersion());
			}

			String destDir = param.getDestDir();
			if (destDir == null || destDir.isEmpty()) {
				// 未指定の場合、デフォルトを設定
				destDir = SourceBuilderConstants.DEST_DIR;

				File dir = new File(destDir);
				if (!dir.exists()) {
					// 該当ディレクトリ無い場合は作成
					dir.mkdirs();
				}
			}

			String baseDir = param.getConfigBaseDir();
			if (baseDir == null || baseDir.isEmpty()) {
				baseDir = SourceBuilderConstants.CONFIG_BASE_DIR;
			}

			// テンプレートファイルルートの設定
			velocityBuildService.setLoaderPath(baseDir);
			// module定義(construction.xml)ファイルの読み込み
			moduleService.setConstructionPath(baseDir, param.getConstructionFileName());

			String[] selctedModuleNames = param.getModules();

			// デバッグ版の作成
			logger.debug(ArrayUtils.toString(selctedModuleNames));
			String[] fileNames = moduleService.getFileNames(selctedModuleNames);
			String[] contents = moduleService.getContents(param.getJsSrcDir(), fileNames);
			Map<String, Object> map = createCommonParamMap(selctedModuleNames, contents, param);
			String jsTemplateFileName = param.getJsTemplateFileName();
			String debug;
			if (jsTemplateFileName != null) {
				debug = velocityBuildService.build(map, jsTemplateFileName);
			} else {
				String content = "";
				for (String content2 : contents) {
					content = content + content2;
				}
				debug = createWriteString(content);
			}
			String dstFileName = param.getJsDevFileName();
			if (dstFileName == null) {
				if (param.getVersion() == null) {
					dstFileName = SourceBuilderConstants.BUILD_DEBUG_FILE_NAME;
				} else {
					dstFileName = buildNameService.getBuildDebugFileName();
				}
			}
			FileUtils.write(new File(destDir, dstFileName), debug, SourceBuilderConstants.ENCODING);

			// リリース版の作成
			OutputSource source = builderService.releaseCompress(debug, param.isTempFileFlag(), true, dstFileName);
			map = new HashMap<String, Object>();
			map.put("version", param.getVersion());
			map.put("content", source.getSource());
			if (selctedModuleNames != null) {
				selctedModuleNames = moduleService.getSortedModuleNames(selctedModuleNames);
				map.put("moduleNames", StringUtils.join(selctedModuleNames, ','));
			}
			map.putAll(param.getVelocityParameter());
			String minHeaderFileName = param.getMinHeaderFileName();
			String release = null;
			if (minHeaderFileName != null) {
				release = velocityBuildService.build(map, minHeaderFileName);
			} else {
				release = createWriteString(source.getSource());
			}
			dstFileName = param.getJsReleaseFileName();
			if (dstFileName == null) {
				if (param.getVersion() == null) {
					dstFileName = SourceBuilderConstants.BUILD_JS_RELEASE_FILE_NAME;
				} else {
					dstFileName = buildNameService.getBuildReleaseFileName();
				}
			}
			FileUtils.write(new File(destDir, dstFileName), release, SourceBuilderConstants.ENCODING);

			// sourceMap出力
			writeSourceMap(destDir, dstFileName, source.getSourceMap());

			// CSSの生成
			String[] cssFileNames = moduleService.getCssFileNames(selctedModuleNames);
			String[] cssContents = moduleService.getContents(param.getCssSrcDir(), cssFileNames);
			String cssTemplateFileName = param.getCssTemplateFileName();
			String cssDebug;
			if (cssTemplateFileName != null) {
				cssDebug = velocityBuildService.build(createCommonParamMap(selctedModuleNames, cssContents, param),
						cssTemplateFileName);
			} else {
				String content = "";
				for (String content2 : cssContents) {
					content = content + content2;
				}
				cssDebug = createWriteString(content);
			}
			dstFileName = param.getCssReleaseFileName();
			if (dstFileName == null) {
				if (param.getVersion() == null) {
					dstFileName = SourceBuilderConstants.BUILD_CSS_RELEASE_FILE_NAME;
				} else {
					dstFileName = buildNameService.getBuildCssFileName();
				}
			}
			FileUtils.write(new File(destDir, dstFileName), cssDebug, SourceBuilderConstants.ENCODING);

			// テンプレートエンジンファイル(EJSライブラリのjsファイル)の生成
			String templateEngineSrcFileName = param.getTemplateEngineSrcFileName();
			if (templateEngineSrcFileName == null) {
				templateEngineSrcFileName = SourceBuilderConstants.TEMPLATE_ENGINE_FILE;
			}
			String[] ejsSrcArray = new String[] { templateEngineSrcFileName };

			String[] ejsContents = moduleService.getContents(param.getTemplateEngineSrcDir(), ejsSrcArray);

			// ejsContentsは一つだが配列のため1つ目を固定で渡す。
			source = builderService.templateEngineCompress(ejsContents[0], param.isTempFileFlag(),
					templateEngineSrcFileName);
			map = new HashMap<String, Object>();
			map.put("version", param.getTemplateEngineVersion());
			map.put("content", source.getSource());
			if (selctedModuleNames != null) {
				selctedModuleNames = moduleService.getSortedModuleNames(selctedModuleNames);
				map.put("moduleNames", StringUtils.join(selctedModuleNames, ','));
			}
			map.putAll(param.getVelocityParameter());
			String templateEngineFile = param.getTemplateEngineFile();
			String ejs;
			if (templateEngineFile != null) {
				ejs = velocityBuildService.build(map, templateEngineFile);
			} else {
				ejs = createWriteString(source.getSource());
			}
			dstFileName = param.getTemplateEngineFileName();
			if (dstFileName == null) {
				if (param.getVersion() == null) {
					dstFileName = SourceBuilderConstants.BUILD_TEMPLATE_ENGINE_FILE_NAME;
				} else {
					dstFileName = buildNameService.getBuildEjsFileName();
				}
			}

			FileUtils.write(new File(destDir, dstFileName), ejs, SourceBuilderConstants.ENCODING);

		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	private String createWriteString(String source) {

		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		try {
			bw.write(source);
			bw.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sw.toString();
	}

	private void writeSourceMap(String dstDir, String dstFileName, SourceMap sourceMap) {

		File file = new File(dstDir, dstFileName + SourceBuilderConstants.EXT_SOURCE_MAP);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file), SourceBuilderConstants.ENCODING));
			sourceMap.appendTo(bw, dstFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private Map<String, Object> createCommonParamMap(String[] moduleNames, String[] contents,
			BuildParameter parameter) {

		moduleNames = moduleService.getSortedModuleNames(moduleNames);
		List<String> files = new ArrayList<String>();
		for (String content : contents) {
			// contentを変換.
			content = contentCleanService.removeFileHeader(content);
			files.add(content);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS (Z)");
		String time = sdf.format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", parameter.getVersion());
		map.put("time", time);
		map.put("files", files);
		map.put("moduleNames", StringUtils.join(moduleNames, ','));
		map.putAll(parameter.getVelocityParameter());
		return map;
	}

	public void setModuleService(ModuleService moduleService) {

		this.moduleService = moduleService;
	}

	public void setBuilderService(BuilderService builderService) {

		this.builderService = builderService;
	}

	public void setBuildNameService(BuildNameService buildNameService) {

		this.buildNameService = buildNameService;
	}

	public void setContentCleanService(ContentCleanService contentCleanService) {

		this.contentCleanService = contentCleanService;
	}

	public void setAntVersionService(AntVersionService antVersionService) {

		this.antVersionService = antVersionService;
	}

	public void setVelocityBuildService(VelocityBuildService velocityBuildService) {

		this.velocityBuildService = velocityBuildService;
	}

}
