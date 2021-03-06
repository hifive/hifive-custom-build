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
package com.htmlhifive.sourcebuilder.model;

import com.google.javascript.jscomp.SourceMap;

public class OutputSource {

	private String source;
	private SourceMap sourceMap;

	public String getSource() {

		return source;
	}

	public void setSource(String source) {

		this.source = source;
	}

	public SourceMap getSourceMap() {

		return sourceMap;
	}

	public void setSourceMap(SourceMap sourceMap) {

		this.sourceMap = sourceMap;
	}



}
