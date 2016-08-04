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
import org.apache.tools.ant.BuildException;

/**
 * <H3>TODO: クラスの概要を記述.</H3>
 *
 * @author fukuda
 */
public class BuildParameter {

	private String jsSrcDir = "";

	private String cssSrcDir = "";

	private String templateEngineSrcDir = "";
	private String destDir = null;

	private String version;
	private String templateEngineVersion;
	private String[] modules = new String[] { "scopedglobals", "util", "async", "resource", "controller", "dataModel",
			"modelWithBinding", "view", "ui", "api.geo", "api.sqldb", "api.storage", "scene", "validation", "dev" };

	private boolean tempFileFlag = false;
	private String jsReleaseFileName = null;

	private String jsDevFileName = null;

	private String cssReleaseFileName = null;

	private String templateEngineFileName = null;

	private String jsTemplateFileName = null;

	private String cssTemplateFileName = null;

	private String templateEngineFile = null;
	private String templateEngineSrcFileName = null;

	private String minHeaderFileName = null;

	private String constructionFileName = null;

	private String configBaseDir = null;

	public String getConfigBaseDir() {

		return configBaseDir;
	}

	public void setConfigBaseDir(String templateRootDir) {

		this.configBaseDir = templateRootDir;
	}

	public String getConstructionFileName() {

		return constructionFileName;
	}

	public void setConstructionFileName(String constructionFileName) {

		this.constructionFileName = constructionFileName;
	}

	public String getJsTemplateFileName() {

		return jsTemplateFileName;
	}

	public void setJsTemplateFileName(String jsTemplateFileName) {

		this.jsTemplateFileName = jsTemplateFileName;
	}

	public String getCssTemplateFileName() {

		return cssTemplateFileName;
	}

	public void setCssTemplateFileName(String cssTemplateFileName) {

		this.cssTemplateFileName = cssTemplateFileName;
	}

	private final Map<String, Object> velocityParameter = new HashMap<String, Object>();

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this);
	}

	public void setModules(String modules) {

		this.modules = StringUtils.split(modules, ",");
	}

	/**
	 * jsSrcDirを取得する.
	 *
	 * @return jsSrcDir
	 */
	public String getJsSrcDir() {

		return jsSrcDir;
	}

	/**
	 * jsSrcDirを設定する.
	 *
	 * @param jsSrcDir jsSrcDir
	 */
	public void setJsSrcDir(String jsSrcDir) {

		this.jsSrcDir = jsSrcDir;
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
	 * cssSrcDirを取得する.
	 *
	 * @return cssSrcDir
	 */
	public String getCssSrcDir() {

		return cssSrcDir;
	}

	/**
	 * cssSrcDirを設定する.
	 *
	 * @param cssSrcDir cssSrcDir
	 */
	public void setCssSrcDir(String cssSrcDir) {

		this.cssSrcDir = cssSrcDir;
	}

	/**
	 * destDirを取得する.
	 *
	 * @return destDir
	 */
	public String getDestDir() {

		return destDir;
	}

	/**
	 * destDirを設定する.
	 *
	 * @param destDir destDir
	 */
	public void setDestDir(String destDir) {

		this.destDir = destDir;
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
	 * modulesを取得する.
	 *
	 * @return modules
	 */
	public String[] getModules() {

		return modules;
	}

	/**
	 * modulesを設定する.
	 *
	 * @param modules modules
	 */
	public void setModules(String[] modules) {

		this.modules = modules;
	}

	/**
	 * tempFileFlagを取得する.
	 *
	 * @return tempFileFlag
	 */
	public boolean isTempFileFlag() {

		return tempFileFlag;
	}

	/**
	 * tempFileFlagを設定する.
	 *
	 * @param tempFileFlag tempFileFlag
	 */
	public void setTempFileFlag(boolean tempFileFlag) {

		this.tempFileFlag = tempFileFlag;
	}

	/**
	 * jsReleaseFileNameを取得する.
	 *
	 * @return jsReleaseFileName
	 */
	public String getJsReleaseFileName() {

		return jsReleaseFileName;
	}

	/**
	 * jsReleaseFileNameを設定する.
	 *
	 * @param jsReleaseFileName jsReleaseFileName
	 */
	public void setJsReleaseFileName(String jsReleaseFileName) {

		this.jsReleaseFileName = jsReleaseFileName;
	}

	/**
	 * jsDevFileNameを取得する.
	 *
	 * @return jsDevFileName
	 */
	public String getJsDevFileName() {

		return jsDevFileName;
	}

	/**
	 * jsDevFileNameを設定する.
	 *
	 * @param jsDevFileName jsDevFileName
	 */
	public void setJsDevFileName(String jsDevFileName) {

		this.jsDevFileName = jsDevFileName;
	}

	/**
	 * cssReleaseFileNameを取得する.
	 *
	 * @return cssReleaseFileName
	 */
	public String getCssReleaseFileName() {

		return cssReleaseFileName;
	}

	/**
	 * cssReleaseFileNameを設定する.
	 *
	 * @param cssReleaseFileName cssReleaseFileName
	 */
	public void setCssReleaseFileName(String cssReleaseFileName) {

		this.cssReleaseFileName = cssReleaseFileName;
	}

	/**
	 * templateEngineFileNameを取得する.
	 *
	 * @return templateEngineFileName
	 */
	public String getTemplateEngineFileName() {

		return templateEngineFileName;
	}

	/**
	 * templateEngineFileNameを設定する.
	 *
	 * @param templateEngineFileName templateEngineFileName
	 */
	public void setTemplateEngineFileName(String templateEngineFileName) {

		this.templateEngineFileName = templateEngineFileName;
	}

	/**
	 * velocityParameterを取得する.
	 *
	 * @return velocityParameter
	 */
	public Map<String, Object> getVelocityParameter() {

		return velocityParameter;
	}

	public String getMinHeaderFileName() {

		return minHeaderFileName;
	}

	public void setMinHeaderFileName(String minHeaderFileName) {

		this.minHeaderFileName = minHeaderFileName;
	}

	/**
	 * 必須パラメータのチェックをする。 必須パラメータがない場合はBuildExceptionをスローする。
	 *
	 */
	public void checkParam() {

		String message = "";

		Map<String, String> paramMap = new HashMap<String, String>();

		if (jsSrcDir == null || jsSrcDir.isEmpty()) {
			paramMap.put("jsSrcDir", "jsSrcDir");
		}

		if (cssSrcDir == null || cssSrcDir.isEmpty()) {
			paramMap.put("cssSrcDir", "cssSrcDir");
		}

		if (templateEngineSrcDir == null || templateEngineSrcDir.isEmpty()) {
			paramMap.put("templateEngineSrcDir", "templateEngineSrcDir");
		}

		if (constructionFileName == null || constructionFileName.isEmpty()) {
			paramMap.put("constructionFileName", "constructionFileName");
		}

		if (paramMap.keySet().size() != 0) {
			// 必須がない場合 必須のキーを合わせたstringを作成
			boolean flag = true;

			for (String key : paramMap.keySet()) {
				String st = "";
				if (!flag) {
					st = ",";
				} else {
					flag = false;
				}
				message = message + st + key;
			}

			throw new BuildException("必須パラメータを入力してください。入力漏れパラメータ：" + message);
		}
	}

	public String getTemplateEngineFile() {

		return templateEngineFile;
	}

	public void setTemplateEngineFile(String templateEngineFile) {

		this.templateEngineFile = templateEngineFile;
	}

	public String getTemplateEngineSrcFileName() {

		return templateEngineSrcFileName;
	}

	public void setTemplateEngineSrcFileName(String templateEngineSrcFileName) {

		this.templateEngineSrcFileName = templateEngineSrcFileName;
	}

}
