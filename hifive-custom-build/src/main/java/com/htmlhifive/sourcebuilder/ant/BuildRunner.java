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

			String baseDir = param.getConfigBaseDir();
			if (baseDir != null) {
				// テンプレートファイルルートの設定
				velocityBuildService.setLoaderPath(baseDir);
				// module定義(construction.xml)ファイルの読み込み
				moduleService.setConstructionPath(baseDir, param.getConstructionFile());
			}

			String[] selctedModuleNames = param.getModuleNames();

			// デバッグ版の作成
			logger.debug(ArrayUtils.toString(selctedModuleNames));
			String[] fileNames = moduleService.getFileNames(selctedModuleNames);
			String[] contents = moduleService.getContents(param.getJsDir(), fileNames);
			Map<String, Object> map = createCommonParamMap(selctedModuleNames, contents, param);
			String debug = velocityBuildService.build(map, SourceBuilderConstants.TEMPLATE_H5_FILE_NAME);
			String dstFileName = param.getDevName();
			if (dstFileName == null) {
				dstFileName = buildNameService.getBuildDebugFileName();
			}
			FileUtils.write(new File(param.getDstDir(), dstFileName), debug, SourceBuilderConstants.ENCODING);
			// リリース版の作成
			OutputSource source = builderService.releaseCompress(debug, param.isTmpFiles(), true, dstFileName);
			map = new HashMap<String, Object>();
			map.put("version", param.getVersion());
			map.put("content", source.getSource());
			if (selctedModuleNames != null) {
				selctedModuleNames = moduleService.getSortedModuleNames(selctedModuleNames);
				map.put("moduleNames", StringUtils.join(selctedModuleNames, ','));
			}
			map.putAll(param.getVelocityParameter());
			String release = velocityBuildService.build(map, SourceBuilderConstants.TEMPLATE_MIN_HEADER_FILE_NAME);
			dstFileName = param.getReleaseName();
			if (dstFileName == null) {
				dstFileName = buildNameService.getBuildReleaseFileName();
			}
			FileUtils.write(new File(param.getDstDir(), dstFileName), release, SourceBuilderConstants.ENCODING);

			// sourceMap出力
			writeSourceMap(param.getDstDir(), dstFileName, source.getSourceMap());

			// CSSの生成
			String[] cssFileNames = moduleService.getCssFileNames(selctedModuleNames);
			String[] cssContents = moduleService.getContents(param.getCssDir(), cssFileNames);
			String cssDebug =
					velocityBuildService.build(createCommonParamMap(selctedModuleNames, cssContents, param),
							SourceBuilderConstants.TEMPLATE_H5CSS_FILE_NAME);
			dstFileName = param.getCssName();
			if (dstFileName == null) {
				dstFileName = buildNameService.getBuildCssFileName();
			}
			FileUtils.write(new File(param.getDstDir(), dstFileName), cssDebug, SourceBuilderConstants.ENCODING);

			// テンプレートエンジンファイル(EJSライブラリのjsファイル)の生成
			String[] ejss =
					moduleService.getContents(param.getTemplateEngineSrcDir(), SourceBuilderConstants.TEMPLATE_ENGINE_FILES);
			source = builderService.templateEngineCompress(ejss[0], param.isTmpFiles(),
					SourceBuilderConstants.TEMPLATE_ENGINE_FILES[0]);
			map = new HashMap<String, Object>();
			map.put("version", param.getTemplateEngineVersion());
			map.put("content", source.getSource());
			if (selctedModuleNames != null) {
				selctedModuleNames = moduleService.getSortedModuleNames(selctedModuleNames);
				map.put("moduleNames", StringUtils.join(selctedModuleNames, ','));
			}
			map.putAll(param.getVelocityParameter());
			String ejs = velocityBuildService.build(map, SourceBuilderConstants.TEMPLATE_TEMPLATE_ENGINE_HEADER_FILE_NAME);
			dstFileName = param.getTemplateEngineName();
			if (dstFileName == null) {
				dstFileName = buildNameService.getBuildEjsFileName();
			}

			FileUtils.write(new File(param.getDstDir(), dstFileName), ejs, SourceBuilderConstants.ENCODING);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeSourceMap(String dstDir, String dstFileName, SourceMap sourceMap) {

		File file = new File(dstDir, dstFileName + SourceBuilderConstants.EXT_SOURCE_MAP);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), SourceBuilderConstants.ENCODING));
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

	private Map<String, Object> createCommonParamMap(String[] moduleNames, String[] contents, BuildParameter parameter) {

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
