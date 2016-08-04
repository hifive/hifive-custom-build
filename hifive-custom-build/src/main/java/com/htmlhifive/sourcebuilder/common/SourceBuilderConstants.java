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
package com.htmlhifive.sourcebuilder.common;

public final class SourceBuilderConstants {

	/** 文字コード. */
	public static final String ENCODING = "UTF-8";

	public static final String BLOCK_COMMENT_BEGIN = "/*";

	public static final String BLOCK_COMMENT_END = "*/";

	public static final String REMOVE_COMMENT_BEGIN = "/* del begin */";

	public static final String REMOVE_COMMENT_END = "/* del end */";

	public static final String BUILD_SOURCE_MAP_DUMMY_PATH = "./dummy.map";

	public static final String EXT_SOURCE_MAP = ".map";

	public static final String VERSION = "${version}";

	public static final String BUILD_TEMPLATE_ENGINE_FILE_NAME = "build-template-engine.js";
	public static final String BUILD_VERSION_TEMPLATE_ENGINE_FILE_NAME = "build-" + VERSION + "-template-engine.js";

	public static final String BUILD_CSS_RELEASE_FILE_NAME = "build.css";
	public static final String BUILD_VERSION_CSS_RELEASE_FILE_NAME = "build_" + VERSION + ".css";

	public static final String BUILD_JS_RELEASE_FILE_NAME = "build.min.js";
	public static final String BUILD_VERSION_JS_RELEASE_FILE_NAME = "build_" + VERSION + "_min.js";

	public static final String BUILD_DEBUG_FILE_NAME = "build.dev.js";
	public static final String BUILD_VERSION_DEBUG_FILE_NAME = "build_" + VERSION + "_dev.js";

	public static final String DEST_DIR = "./build";

	public static final String CONFIG_BASE_DIR = "./";

	// No19 addVersionToFileName (追加)
	public static final Boolean ADD_VERSION_TO_FILE_NAME = true;

	public static final String TEMPLATE_ENGINE_FILE = "ejs-1.0.js";

	public static final String BUILD_SOURCE_MAP_FILE_NAME = BUILD_VERSION_JS_RELEASE_FILE_NAME + ".map";

	private SourceBuilderConstants() {

	}

}
