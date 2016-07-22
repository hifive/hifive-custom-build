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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.htmlhifive.sourcebuilder.common.SourceBuilderConstants;

public class VelocityBuildService {

	@Inject
	@Named("velocityProperties")
	private Properties properties;

	private VelocityEngine velocityEngine = new VelocityEngine();

	/**
	 * 初期化処理.
	 */
	@PostConstruct
	public void init() {

		setNullLog();
		velocityEngine.init(properties);
	}

	private void setNullLog() {

		velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.NullLogSystem");
	}

	public void setLoaderPath(String path) {

		Properties additionalProperties = new Properties();
		additionalProperties.setProperty("file.resource.loader.path", path);
		VelocityEngine engine = new VelocityEngine();
		this.velocityEngine = engine;
		setNullLog();
		engine.init(additionalProperties);
	}

	public String build(Map<String, Object> paramMap, String templateFilePath) throws IOException {

		return build(paramMap, templateFilePath, SourceBuilderConstants.ENCODING);
	}

	public String build(Map<String, Object> paramMap, String templateFilePath, String encoding) throws IOException {

		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		try {
			VelocityContext context = new VelocityContext();
			for (Entry<String, Object> entry : paramMap.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}

			// テンプレートの作成
			Template template = velocityEngine.getTemplate(templateFilePath, encoding);
			// テンプレートとマージ
			template.merge(context, bw);
			bw.flush();
			return sw.toString();

		} finally {
			if (null != bw) {
				bw.close();
			}
		}
	}

	public void setProperties(Properties properties) {

		this.properties = properties;
	}

}
