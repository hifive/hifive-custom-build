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
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.htmlhifive.sourcebuilder.common.SourceBuilderConstants;
import com.htmlhifive.sourcebuilder.model.OutputSource;

public class CompressService {

	private CompilationLevel compilationLevel = CompilationLevel.SIMPLE_OPTIMIZATIONS;

	public OutputSource compressJsOutputTmp(String content, String encoding, String name) throws IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
		String time = sdf.format(new Date());

		String tmpSrcPath = name + "_src.tmp" + time;
		String tmpDstPath = name + "_dst.tmp" + time;
		File tmpSrcFile = new File(tmpSrcPath);
		File tmpDstFile = new File(tmpDstPath);
		FileUtils.write(tmpSrcFile, content, encoding);
		OutputSource compressContent = compressJs(content, encoding, name);
		FileUtils.write(tmpDstFile, compressContent.getSource());
		return compressContent;
	}

	public OutputSource compressJs(String content, String encoding, String dstFileName) {

		OutputSource source = new OutputSource();

		Compiler.setLoggingLevel(Level.FINEST);
		CompilerOptions options = new CompilerOptions();
		Compiler compiler = new Compiler();
		this.compilationLevel.setOptionsForCompilationLevel(options);
		List<SourceFile> externs = new ArrayList<SourceFile>();
		List<SourceFile> sources = new ArrayList<SourceFile>();
		sources.add(SourceFile.fromCode(dstFileName, content));
		Charset outputCharset = Charset.forName(encoding);
		options.setOutputCharset(outputCharset);

		// sourceMap作成時、下記パラメータがない場合、.getSourceMap()がnullとなる。
		// ここではsourceMap出力するわけではないため、一時的にダミーのパスを指定する。
		options.setSourceMapOutputPath(SourceBuilderConstants.BUILD_SOURCE_MAP_DUMMY_PATH);

		compiler.compile(externs, sources, options);

		source.setSource(compiler.toSource());
		source.setSourceMap(compiler.getSourceMap());

		return source;
	}

	/**
	 * "simple"か"advanced"か"whitespace"のいずれか.<br>
	 * デフォルトはsimple.
	 *
	 * @param level
	 */
	public void setCompilationLevel(String level) {

		if (StringUtils.equals("simple", level)) {
			this.compilationLevel = CompilationLevel.SIMPLE_OPTIMIZATIONS;
		} else if (StringUtils.equals("advanced", level)) {
			this.compilationLevel = CompilationLevel.ADVANCED_OPTIMIZATIONS;
		} else if (StringUtils.equals("whitespace", level)) {
			this.compilationLevel = CompilationLevel.WHITESPACE_ONLY;
		}
		throw new IllegalArgumentException();
	}
}
