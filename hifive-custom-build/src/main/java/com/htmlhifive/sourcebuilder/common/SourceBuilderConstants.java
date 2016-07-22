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

	public static final String FILE_RESOURCE_LOADER_PATH = "FILE.resource.loader.path";

	public static final String TEMPLATE_H5_FILE_NAME = "template/h5.vm";

	public static final String TEMPLATE_MIN_HEADER_FILE_NAME = "template/min-header.vm";

	public static final String TEMPLATE_TEMPLATE_ENGINE_HEADER_FILE_NAME = "template/ejs-header.vm";

	public static final String TEMPLATE_H5CSS_FILE_NAME = "template/h5css.vm";

	public static final String CONFIG_FILE = "h5-file-construction.xml";

	public static final String[] TEMPLATE_ENGINE_FILES = new String[] { "ejs-1.0.js" };

	public static final String VERSION = "${version}";

	public static final String BUILD_DEBUG_FILE_NAME = "h5" + VERSION + ".dev.js";

	public static final String BUILD_DEBUG_CSS_FILE_NAME = "h5" + VERSION + ".css";

	public static final String BUILD_RELEASE_FILE_NAME = "h5" + VERSION + ".js";

	public static final String BUILD_TEMPLATE_ENGINE_FILE_NAME = "ejs" + VERSION + ".h5mod.js";

	public static final String BUILD_SOURCE_MAP_FILE_NAME = BUILD_RELEASE_FILE_NAME + ".map";

	public static final String BUILD_SOURCE_MAP_DUMMY_PATH = "./dummy.map";

	public static final String BUILDE_CONTEXT_PATH = "classpath:spring/build-context.xml";

	public static final String GENERIC_BUILDE_CONTEXT_PATH = "classpath:spring/generic-build-context.xml";

	public static final String EXT_SOURCE_MAP = ".map";

	private SourceBuilderConstants() {

	}

}
