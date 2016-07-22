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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.htmlhifive.sourcebuilder.common.SourceBuilderConstants;

public class ContentCleanService {

	private static Logger logger = LoggerFactory.getLogger(ContentCleanService.class);

	@Inject
	private ModuleService moduleService;

	@Inject
	private VelocityBuildService velocityBuildService;

	@Inject
	private VersionService versionService;

	/**
	 *
	 * @param content
	 * @return
	 */
	public String removeBeginEnd(String content) {

		int beginIndex = -1;
		int endIndex = -1;
		while (-1 != (beginIndex = content.indexOf(SourceBuilderConstants.REMOVE_COMMENT_BEGIN))) {
			if (-1 == (endIndex = content.indexOf(SourceBuilderConstants.REMOVE_COMMENT_END))) {
				break;
			}
			if (beginIndex > endIndex) {
				break;
			}
			String removedString =
					content.substring(beginIndex, endIndex + SourceBuilderConstants.REMOVE_COMMENT_END.length());
			logger.debug("remove [" + removedString + "]");
			content =
					content.substring(0, beginIndex)
					+ content.substring(endIndex + SourceBuilderConstants.REMOVE_COMMENT_END.length());
		}
		return content;
	}

	/**
	 * ファイルヘッダーの除去.
	 *
	 * @param content
	 * @return
	 */
	public String removeFileHeader(String content) {

		// シングルクォート、ダブルクォート、大文字、小文字に関わらず除去
		content = StringUtils.removeStartIgnoreCase(content, "@charset \'utf-8\';");
		content = StringUtils.removeStartIgnoreCase(content, "@charset \"utf-8\";");
		content = content.trim();
		if (content.startsWith(SourceBuilderConstants.BLOCK_COMMENT_BEGIN)) {
			int index = content.indexOf(SourceBuilderConstants.BLOCK_COMMENT_END);
			if (-1 != index) {
				content = content.substring(index + 2);
			}
		}
		return content.trim();
	}

	public void setModuleService(ModuleService moduleService) {

		this.moduleService = moduleService;
	}

	public void setVelocityBuildService(VelocityBuildService velocityBuildService) {

		this.velocityBuildService = velocityBuildService;
	}

	public void setVersionService(VersionService versionService) {

		this.versionService = versionService;
	}

}
