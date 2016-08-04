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

import javax.inject.Inject;

import com.htmlhifive.sourcebuilder.common.SourceBuilderConstants;
import com.htmlhifive.sourcebuilder.exception.H5FileIOException;

public class BuildNameService {

	@Inject
	private VersionService versionService;

	public String getBuildCssFileName() throws H5FileIOException {

		return getBuildFileName(SourceBuilderConstants.BUILD_VERSION_CSS_RELEASE_FILE_NAME,
				versionService.getCurrentVersion());
	}

	public String getBuildEjsFileName() {

		return getBuildFileName(SourceBuilderConstants.BUILD_VERSION_TEMPLATE_ENGINE_FILE_NAME,
				versionService.getTemplateEngineVersion());
	}

	public String getBuildDebugFileName() throws H5FileIOException {

		return getBuildFileName(SourceBuilderConstants.BUILD_VERSION_DEBUG_FILE_NAME,
				versionService.getCurrentVersion());
	}

	public String getBuildReleaseFileName() throws H5FileIOException {

		return getBuildFileName(SourceBuilderConstants.BUILD_VERSION_JS_RELEASE_FILE_NAME,
				versionService.getCurrentVersion());
	}

	public String getBuildFileName(String base, String version) {

		if (null == version) {
			return base.replace(SourceBuilderConstants.VERSION, "");
		}
		return base.replace(SourceBuilderConstants.VERSION, version);
	}

	public void setVersionService(VersionService versionService) {

		this.versionService = versionService;
	}

	public String getBuildSourceMapFileName() throws H5FileIOException {

		return getBuildFileName(SourceBuilderConstants.BUILD_SOURCE_MAP_FILE_NAME, versionService.getCurrentVersion());
	}

}
