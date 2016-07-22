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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <H3>
 * TODO: クラスの概要を記述.</H3>
 *
 * @author fukuda
 */
public class BuildParameter {

	private String jsDir = "src/main/webapp/src";
	private String cssDir = "src/main/webapp/srcCss";
	private String templateEngineSrcDir = "src/main/webapp/src/ejs";
	private String dstDir = "src/main/webapp/release";
	private String version;
	private String templateEngineVersion;
	private String[] moduleNames = new String[] { "util", "controller", "data", "view", "ui", "api.geo", "api.sqldb",
	"api.storage" };
	private boolean tmpFiles = false;
	private String releaseName = null;
	private String devName = null;
	private String cssName = null;
	private String templateEngineName = null;
	private String jsTemplateFile = null;
	private String cssTemplateFile = null;

	private String minHeaderFile = null;
	private String constructionFile = null;
	private String configBaseDir = null;

	public String getConfigBaseDir() {

		return configBaseDir;
	}

	public void setConfigBaseDir(String templateRootDir) {

		this.configBaseDir = templateRootDir;
	}

	public String getConstructionFile() {

		return constructionFile;
	}

	public void setConstructionFile(String configPath) {

		this.constructionFile = configPath;
	}

	public void setJsTemplateFile(String srcTemplate) {

		this.jsTemplateFile = srcTemplate;
	}

	public void setCssTemplateFile(String cssTemplate) {

		this.cssTemplateFile = cssTemplate;
	}

	public String getJsTemplateFile() {

		return jsTemplateFile;
	}

	public String getCssTemplateFile() {

		return cssTemplateFile;
	}

	private final Map<String, Object> velocityParameter = new HashMap<String, Object>();

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this);
	}

	public void setModuleNames(String moduleNames) {

		this.moduleNames = StringUtils.split(moduleNames, ",");
	}

	/**
	 * JsDirを取得する.
	 *
	 * @return jsDir
	 */
	public String getJsDir() {

		return jsDir;
	}

	/**
	 * jsDirを設定する.
	 *
	 * @param jsDir jsDir
	 */
	public void setJsDir(String jsDir) {

		this.jsDir = jsDir;
	}

	/**
	 * templateEngineSrcDirを取得する.
	 *
	 * @return templateEngineSrcDir
	 */
	public String getTemplateEngineSrcDir() {

		return templateEngineSrcDir;
	}

	/**
	 * templateEngineSrcDirを設定する.
	 *
	 * @param templateEngineSrcDir templateEngineSrcDir
	 */
	public void setTemplateEngineSrcDir(String templateEngineSrcDir) {

		this.templateEngineSrcDir = templateEngineSrcDir;
	}

	/**
	 * cssDirを取得する.
	 *
	 * @return cssSrcDir
	 */
	public String getCssDir() {

		return cssDir;
	}

	/**
	 * cssSrcDirを設定する.
	 *
	 * @param cssDir cssDir
	 */
	public void setCssDir(String cssDir) {

		this.cssDir = cssDir;
	}

	/**
	 * dstDirを取得する.
	 *
	 * @return dstDir
	 */
	public String getDstDir() {

		return dstDir;
	}

	/**
	 * dstDirを設定する.
	 *
	 * @param dstDir dstDir
	 */
	public void setDstDir(String dstDir) {

		this.dstDir = dstDir;
	}

	/**
	 * versionを取得する.
	 *
	 * @return version
	 */
	public String getVersion() {

		return version;
	}

	/**
	 * versionを設定する.
	 *
	 * @param version version
	 */
	public void setVersion(String version) {

		this.version = version;
	}

	/**
	 * templateEngineVersionを取得する.
	 *
	 * @return templateEngineVersion
	 */
	public String getTemplateEngineVersion() {

		return templateEngineVersion;
	}

	/**
	 * templateEngineVersionを設定する.
	 *
	 * @param templateEngineVersion templateEngineVersion
	 */
	public void setTemplateEngineVersion(String templateEngineVersion) {

		this.templateEngineVersion = templateEngineVersion;
	}

	/**
	 * moduleNamesを取得する.
	 *
	 * @return moduleNames
	 */
	public String[] getModuleNames() {

		return moduleNames;
	}

	/**
	 * moduleNamesを設定する.
	 *
	 * @param moduleNames moduleNames
	 */
	public void setModuleNames(String[] moduleNames) {

		this.moduleNames = moduleNames;
	}

	/**
	 * tmpFilesを取得する.
	 *
	 * @return tmpFiles
	 */
	public boolean isTmpFiles() {

		return tmpFiles;
	}

	/**
	 * tmpFilesを設定する.
	 *
	 * @param tmpFiles tmpFiles
	 */
	public void setTmpFiles(boolean tmpFiles) {

		this.tmpFiles = tmpFiles;
	}

	/**
	 * releaseNameを取得する.
	 *
	 * @return releaseName
	 */
	public String getReleaseName() {

		return releaseName;
	}

	/**
	 * releaseNameを設定する.
	 *
	 * @param releaseName releaseName
	 */
	public void setReleaseName(String releaseName) {

		this.releaseName = releaseName;
	}

	/**
	 * devNameを取得する.
	 *
	 * @return devName
	 */
	public String getDevName() {

		return devName;
	}

	/**
	 * devNameを設定する.
	 *
	 * @param devName devName
	 */
	public void setDevName(String devName) {

		this.devName = devName;
	}

	/**
	 * cssNameを取得する.
	 *
	 * @return cssName
	 */
	public String getCssName() {

		return cssName;
	}

	/**
	 * cssNameを設定する.
	 *
	 * @param cssName cssName
	 */
	public void setCssName(String cssName) {

		this.cssName = cssName;
	}

	/**
	 * templateEngineNameを取得する.
	 *
	 * @return templateEngineName
	 */
	public String getTemplateEngineName() {

		return templateEngineName;
	}

	/**
	 * templateEngineNameを設定する.
	 *
	 * @param templateEngineName templateEngineName
	 */
	public void setTemplateEngineName(String templateEngineName) {

		this.templateEngineName = templateEngineName;
	}

	/**
	 * velocityParameterを取得する.
	 *
	 * @return velocityParameter
	 */
	public Map<String, Object> getVelocityParameter() {

		return velocityParameter;
	}

	public String getMinHeaderFile() {

		return minHeaderFile;
	}

	public void setMinHeaderFile(String minHeader) {

		this.minHeaderFile = minHeader;
	}

}
