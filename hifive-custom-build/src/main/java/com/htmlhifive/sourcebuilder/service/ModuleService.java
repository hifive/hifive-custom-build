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
package com.htmlhifive.sourcebuilder.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

import com.htmlhifive.sourcebuilder.common.SourceBuilderConstants;
import com.htmlhifive.sourcebuilder.exception.H5FileIOException;
import com.htmlhifive.sourcebuilder.model.Dependencies;
import com.htmlhifive.sourcebuilder.model.Dependency;
import com.htmlhifive.sourcebuilder.model.Files;
import com.htmlhifive.sourcebuilder.model.H5FileConstruction;
import com.htmlhifive.sourcebuilder.model.Module;

/**
 * <H3>
 * Moduleやコンテンツの取得をするサービスクラス</H3>
 *
 * @author
 */
public class ModuleService {

	/**
	 * 取得するファイルのタイプ(js,css)
	 */
	private enum FILE_TYPE {
		JS, CSS
	};

	@Inject
	@Named("envProperties")
	private Properties properties;

	/**
	 * H5ファイル構成情報.
	 */
	private H5FileConstruction config = null;

	/**
	 * H5ファイル構成情報参照用のマップ.
	 */
	private Map<String, Module> moduleMap = null;

	@Inject
	private VersionService versionService;

	// -------------------------------------------------------------------------
	/**
	 * 初期化処理.
	 */
	@PostConstruct
	public void init() throws H5FileIOException {

		if (null != config) {
			return;
		}
		refreshConfig();
	}

	/**
	 * configパスの設定.
	 *
	 * @return
	 * @throws H5FileIOException
	 */
	public void setConstructionPath(String configBaseDir, String configPath) throws H5FileIOException {

		properties.setProperty("h5.file.configuration.path", new File(configBaseDir, configPath).getPath());
		refreshConfig();
	}

	/**
	 * モジュール構成定義XMLファイルを読み込む.
	 *
	 * @param path
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public H5FileConstruction readConfig() throws JAXBException, H5FileIOException {

		try {
			InputStream is = null;
			String configurationBase = properties.getProperty("h5.file.configuration.base");
			String configurationFileName = properties.getProperty("h5.file.configuration.path");
			if (configurationFileName != null) {
				String configurationPath = configurationFileName;
				if (configurationBase != null) {
					// プロパティファイルの設定にはバージョンディレクトリは無いので、
					// バージョンディレクトリを追加する。バージョンディレクトリは.を_に置換したもの(1.2.0なら1_2_0)
					configurationPath =
							versionService.addVersionDirectory(configurationBase) + "/" + configurationFileName;
				}
				is = new FileInputStream(new File(configurationPath));

			} else {
				is = this.getClass().getClassLoader().getResourceAsStream(SourceBuilderConstants.CONFIG_FILE);
			}
			JAXBContext jc = JAXBContext.newInstance(H5FileConstruction.class);
			Unmarshaller um = jc.createUnmarshaller();
			H5FileConstruction config = (H5FileConstruction) um.unmarshal(is);
			is.close();
			return config;

		} catch (JAXBException e) {
			throw e;
		} catch (IOException e) {
			throw new H5FileIOException("モジュール定義ファイルにアクセスできませんでした");
		}
	}

	/**
	 *
	 * @return
	 */
	private Map<String, Module> checkConfig() {

		if (null == config) {
			throw new NullPointerException("config is null.");
		}

		Map<String, Module> moduleMap = new HashMap<String, Module>();
		for (Module module : config.getModule()) {
			if (null != moduleMap.get(module.getName())) {
				throw new IllegalArgumentException("duplicated module definition. " + module.getName());
			}
			moduleMap.put(module.getName(), module);
		}
		for (Module module : config.getModule()) {
			Dependencies dependencies = module.getDependencies();
			for (Dependency d : dependencies.getDependency()) {
				String name = d.getName();
				if (null == moduleMap.get(module.getName())) {
					throw new IllegalArgumentException("unknown module dependency. " + name);
				}
			}
		}
		return moduleMap;
	}

	/**
	 *
	 * @param version
	 * @param fileNames
	 * @throws IOException
	 */
	public String[] getContents(String srcDir, String[] fileNames) throws H5FileIOException {

		List<String> files = new ArrayList<String>();
		String webappRoot = properties.getProperty("h5.file.webapp.path");
		if (webappRoot != null) {
			// バージョンディレクトリの追加
			webappRoot = versionService.addVersionDirectory(webappRoot);
		}
		for (String fileName : fileNames) {
			File file;
			if (webappRoot != null) {
				file = new File(webappRoot + "/" + srcDir + "/", fileName);
			} else {
				file = new File(srcDir + "/", fileName);
			}
			String content;
			try {
				content = FileUtils.readFileToString(file, SourceBuilderConstants.ENCODING);
			} catch (IOException e) {
				throw new H5FileIOException("リソースファイルにアクセスできませんでした");
			}
			files.add(content);
		}
		return files.toArray(new String[0]);
	}

	/**
	 *
	 * @param moduleNames
	 * @return
	 */
	public String[] getFileNames(String[] moduleNames) {

		return getFileNames(moduleNames, FILE_TYPE.JS);
	}

	/**
	 *
	 * @param moduleNames
	 * @return
	 */
	public String[] getCssFileNames(String[] moduleNames) {

		return getFileNames(moduleNames, FILE_TYPE.CSS);
	}

	public String[] getSortedModuleNames(String[] moduleNames) {

		if (moduleNames == null) {
			return null;
		}
		return getModuleNamesWithDepended(moduleNames);
	}

	/**
	 *
	 * @param moduleNames
	 * @return
	 */
	private String[] getModuleNamesWithDepended(String[] moduleNames) {

		if (null == config) {
			throw new NullPointerException("config is null.");
		}
		if (null == moduleMap) {
			throw new NullPointerException("moduleMap is null.");
		}

		if (null == moduleNames || 0 == moduleNames.length) {
			return null;
		}
		// 依存関係を元に、必要なモジュール名のセットを生成する.
		ArrayList<String> sortedModuleNames = new ArrayList<String>();
		for (String moduleName : moduleNames) {
			getDependencies(moduleName, sortedModuleNames);
		}
		return sortedModuleNames.toArray(new String[0]);
	}

	/**
	 *
	 * @param module
	 * @param alreadyExpandedModuleNames
	 * @return
	 */
	public void getDependencies(String moduleName, ArrayList<String> alreadyExpandedModuleNames) {

		if (alreadyExpandedModuleNames.contains(moduleName)) {
			return;
		}

		Module module = moduleMap.get(moduleName);
		if (null == module) {
			throw new IllegalArgumentException("unknown module. " + moduleName);
		}

		for (Dependency d : module.getDependencies().getDependency()) {
			String dependModuleName = d.getName();
			if (alreadyExpandedModuleNames.contains(dependModuleName)) {
				continue;
			}
			getDependencies(dependModuleName, alreadyExpandedModuleNames);
		}
		alreadyExpandedModuleNames.add(moduleName);
	}

	/**
	 *
	 * @return
	 */
	public List<Module> getAllModules() throws H5FileIOException {

		if (null == config) {
			throw new NullPointerException("config is null.");
		}

		// configを最新のものに設定する
		refreshConfig();

		return config.getModule();
	}

	private String[] getFileNames(String[] moduleNames, FILE_TYPE type) {

		String[] sortedModuleNames = getModuleNamesWithDepended(moduleNames);
		if (null == config) {
			throw new NullPointerException("config is null.");
		}
		if (null == moduleMap) {
			throw new NullPointerException("moduleMap is null.");
		}

		ArrayList<String> fileList = new ArrayList<String>();
		for (String moduleName : sortedModuleNames) {
			Module module = moduleMap.get(moduleName);
			Files files = null;
			if (type == FILE_TYPE.JS) {
				files = module.getFiles();
			} else {
				files = module.getCssfiles();
			}
			for (com.htmlhifive.sourcebuilder.model.File file : files.getFile()) {
				String fileName = file.getName();
				if (fileList.contains(fileName)) {
					// 別モジュールで同一のファイルが参照されていた場合でも、同一ファイルは含めないようにする
					continue;
				}
				fileList.add(fileName);
			}
		}
		return fileList.toArray(new String[0]);
	}

	/**
	 * configをファイルから読み込んで設定する
	 *
	 * @throws H5FileIOException
	 */
	public void refreshConfig() throws H5FileIOException {

		try {
			config = readConfig();
			moduleMap = Collections.unmodifiableMap(checkConfig());
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public void setProperties(Properties properties) {

		this.properties = properties;
	}

	public void setVersionService(VersionService versionService) {

		this.versionService = versionService;
	}

}
