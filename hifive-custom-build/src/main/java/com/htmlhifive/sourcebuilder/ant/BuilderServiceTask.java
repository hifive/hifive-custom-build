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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Parameter;

import com.htmlhifive.sourcebuilder.exception.H5FileIOException;
import com.htmlhifive.sourcebuilder.service.AntVersionService;
import com.htmlhifive.sourcebuilder.service.BuildNameService;
import com.htmlhifive.sourcebuilder.service.BuilderService;
import com.htmlhifive.sourcebuilder.service.CompressService;
import com.htmlhifive.sourcebuilder.service.ContentCleanService;
import com.htmlhifive.sourcebuilder.service.ModuleService;
import com.htmlhifive.sourcebuilder.service.VelocityBuildService;

public class BuilderServiceTask extends Task {

	private BuildRunner buildRunner = null;

	private BuildParameter param = new BuildParameter();

	public void setTemplateEngineName(String templateEngineName) {

		param.setTemplateEngineName(templateEngineName);
	}

	@Override
	public void init() {

		ClassLoader loader = this.getClass().getClassLoader();
		if (loader instanceof AntClassLoader) {
			((AntClassLoader) loader).setThreadContextLoader();
		}

		buildRunner = new BuildRunner();

		// 各サービスを設定
		AntVersionService antVersionService = new AntVersionService();

		ModuleService moduleService = new ModuleService();
		// envProperties
		InputStream inStream = null;
		inStream = loader.getResourceAsStream("env.properties");
		Properties envProperties = new Properties();
		try {
			envProperties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		moduleService.setProperties(envProperties);
		moduleService.setVersionService(antVersionService);

		// moduleServiceのコンフィグ読み込み
		try {
			moduleService.refreshConfig();
		} catch (H5FileIOException e) {
			e.printStackTrace();
		}

		VelocityBuildService velocityBuildService = new VelocityBuildService();
		// velocityProperties
		inStream = loader.getResourceAsStream("velocity.properties");
		Properties velocityProperties = new Properties();
		try {
			velocityProperties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		velocityBuildService.setProperties(velocityProperties);

		ContentCleanService contentCleanService = new ContentCleanService();
		contentCleanService.setModuleService(moduleService);
		contentCleanService.setVelocityBuildService(velocityBuildService);
		contentCleanService.setVersionService(antVersionService);

		CompressService compressService = new CompressService();

		BuilderService builderService = new BuilderService();
		builderService.setCompressService(compressService);
		builderService.setContentCleanService(contentCleanService);
		builderService.setModuleService(moduleService);
		builderService.setVelocityBuildService(velocityBuildService);
		builderService.setVersionService(antVersionService);

		BuildNameService buildNameService = new BuildNameService();
		buildNameService.setVersionService(antVersionService);

		buildRunner.setContentCleanService(contentCleanService);
		buildRunner.setModuleService(moduleService);
		buildRunner.setVelocityBuildService(velocityBuildService);
		buildRunner.setAntVersionService(antVersionService);
		buildRunner.setBuilderService(builderService);
		buildRunner.setBuildNameService(buildNameService);

	}

	@Override
	public void execute() {

		if (buildRunner != null) {

			buildRunner.execute(param);
		}
	}

	public void addConfiguredVelocityParameter(Parameter parameter) {

		param.getVelocityParameter().put(parameter.getName(), StringUtils.trim(parameter.getValue()));
	}

	public void addConfiguredBuildParameter(BuildParameter buildParameter) {

		this.param = buildParameter;
	}

}
