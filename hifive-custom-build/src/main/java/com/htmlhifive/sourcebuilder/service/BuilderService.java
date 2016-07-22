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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import com.htmlhifive.sourcebuilder.common.SourceBuilderConstants;
import com.htmlhifive.sourcebuilder.model.OutputSource;

public class BuilderService {

	@Inject
	private CompressService compressService;

	@Inject
	private ContentCleanService contentCleanService;

	@Inject
	private ModuleService moduleService;

	@Inject
	private VelocityBuildService velocityBuildService;

	@Inject
	private VersionService versionService;

	/**
	 * 渡された入力ファイル名をそのまま渡して圧縮して返す.
	 *
	 * @param content
	 * @param version
	 * @param deleteTmpFiles
	 * @return
	 * @throws IOException
	 */
	public OutputSource templateEngineCompress(String content, boolean tmpFlg, String dstFileName) throws IOException {

		if (tmpFlg) {
			return compressService.compressJsOutputTmp(content, SourceBuilderConstants.ENCODING, dstFileName);
		}
		return compressService.compressJs(content, SourceBuilderConstants.ENCODING, dstFileName);
	}

	/**
	 * リリース版ビルドの作成.<br>
	 * 一時ファイル生成なし.<br>
	 * 渡された入力ファイル名をそのまま渡して圧縮して返す.
	 *
	 * @param content
	 * @param dstFileName
	 * @param version
	 * @param moduleNames
	 * @param deleteTmpFiles
	 * @return
	 * @throws Exception
	 */
	public OutputSource releaseCompress(final String content, boolean tmpFlg, boolean isFwCode, String dstFileName)
			throws IOException {

		// 1.begin-end抜き
		String cleanContent = contentCleanService.removeBeginEnd(content);
		// 2.ログ抜き
		if (isFwCode) {
			try {
				cleanContent = removeFWLogger(cleanContent);
			} catch (IOException e) {
				throw e;
			} catch (Exception e) {
				System.err.println("ログ抜き処理に失敗しました。クラスパスにh5sourceCodeConverter.jarがあることを確認してください。");
				e.printStackTrace();
				return null;
			}
		}
		if (tmpFlg) {
			return compressService.compressJsOutputTmp(cleanContent, SourceBuilderConstants.ENCODING, dstFileName);
		}

		return compressService.compressJs(cleanContent, SourceBuilderConstants.ENCODING, dstFileName);
	}

	private Map<String, Object> createCommonParamMap(String[] moduleNames, String[] contents, String version) {

		moduleNames = moduleService.getSortedModuleNames(moduleNames);
		List<String> files = new ArrayList<String>();
		for (String content : contents) {
			// contentを変換.
			content = contentCleanService.removeFileHeader(content);
			files.add(content);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS (Z)");
		String time = sdf.format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", version);
		map.put("time", time);
		map.put("files", files);
		map.put("moduleNames", StringUtils.join(moduleNames, ','));

		return map;
	}

	private String removeFWLogger(String content) throws IOException, ClassNotFoundException, NoSuchMethodException,
	SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	InstantiationException, NoSuchFieldException {

		// ログ出力抜き
		// クラスを動的にロードする
		// (このメソッド(getCleanContent)を呼ばない限りcom.htmlhifive.tools.*は不要)
		Class<?> Main = Class.forName("com.htmlhifive.tools.rhino.Main");
		Class<?> SourceMaker = Class.forName("com.htmlhifive.tools.rhino.SourceMaker");
		Class<?> Node = Class.forName("org.mozilla.javascript.Node");
		Method parse = Main.getMethod("parse", new Class[] { String.class, String.class });
		Method toSource = SourceMaker.getDeclaredMethod("toSource", new Class[] { Node });
		return (String) toSource.invoke(null, (parse.invoke(Main.newInstance(), new Object[] { content, "temp" })));
	}

	public void setCompressService(CompressService compressService) {

		this.compressService = compressService;
	}

	public void setContentCleanService(ContentCleanService contentCleanService) {

		this.contentCleanService = contentCleanService;
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
