# hifive-custom-build

・ライセンス

Apache2.0 ライセンス

・準備

	1.ivy_build.xmlのresolveターゲットを実行し、必要なライブラリを取得する。


・ビルド

	1.build.xmlのbuildターゲットを実行する。
	2.buildフォルダ以下にhifive-custom-build.jarができる。


・ビルドにて作成したjarをantタスクにて実行する方法

	-build.xmlの書き方

		1. taskdefを定義し classname="com.htmlhifive.sourcebuilder.ant.BuilderServiceTask" を定義する。

			<taskdef name="buildService" classname="com.htmlhifive.sourcebuilder.ant.BuilderServiceTask" classpathref="base.path" />

		2. 該当のタスクにてbuildParameterとvelocityParameterを定義する。

			<buildService>
				<buildParameter
					tempFileFlag="false"
					templateEngineFileName="ejs-h5mod.js"
					cssReleaseFileName="h5.css"
					jsReleaseFileName="h5.js"
					jsDevFileName="h5.dev.js"
					jsSrcDir="src/main/webapp/src"
					cssSrcDir="src/main/webapp/srcCss"
					templateEngineSrcDir="src/main/webapp/src/ejs"
					destDir="src/main/webapp/archives/current"
					version="1.0"
					templateEngineVersion="1.0"
					configBaseDir="./config/build"
					jsTemplateFileName=="template/h5.vm"
					cssTemplateFileName="template/h5css.vm"
					minHeaderFileName="template/min-header.vm"
					templateEngineFile="template/ejs-header.vm"
					constructionFileName="h5-file-construction.xml"
					templateEngineSrcFileName= "ejs-1.0.js"
					modules="util,async,resource,controller,dataModel,modelWithBinding,view,ui,api.geo,api.sqldb,api.storage,scene,validation" />
				<velocityParameter name="gitCommitId" value="ebce084e74e00017807dab63d358597aef6d26e4" />
			</buildService>


	- パラメータ一覧
		tempFileFlag              ：tmpファイルを作るか否か(true,falseにて指定)
		                           省略時は"false"
		templateEngineFileName   ：ビルドで作成されるEJSファイル名
		                               省略時は"build-template-engine.js"
		cssReleaseFileName       ：ビルドで作成されるCSSファイル名
		                               省略時は"build.css"
		jsReleaseFileName        ：ビルドで作成されるminifyされたJSファイル名
		                               省略時は"build.min.js"
		jsDevFileName            ：ビルドで作成されるminifyする前のJSファイル名
		                               省略時は"build.dev.js"
		jsSrcDir                 ：ビルド元のJSファイルが格納されているディレクトリ(必須パラメータ)
		cssSrcDir                ：ビルド元のCSSファイルが格納されているディレクトリ(必須パラメータ)
		templateEngineSrcDir     ：ビルド元のEJSファイルが格納されているディレクトリ(必須パラメータ)
		destDir                  ：ビルドで作成されるファイルの出力先ディレクトリ
		                               省略時は"./build"
		version                  ：ビルドで作成されるファイル名に含まれる版数(JSファイル用)
		                           省略時はバージョン情報付与しません。
		templateEngineVersion    ：ビルドで作成されるファイル名に含まれる版数(EJS用)
		                           省略時はバージョン情報付与しません。
		configBaseDir            ：コンフィグファイルが格納されているディレクトリ
		                               省略時は"./"
		jsTemplateFileName       ：JSファイルのテンプレートファイル名
		                               省略時はテンプレートは使用しません
		cssTemplateFileName      ：CSSファイルのテンプレートファイル名
		                               省略時はテンプレートは使用しません
		minHeaderFileName        ：ビルドで作成されるファイルのヘッダテンプレートファイル名
		                               省略時はテンプレートは使用しません
		templateEngineFile       ：EJSファイルのテンプレートファイル名
		                               省略時はテンプレートは使用しません
		constructionFileName     ：コンフィグファイル名(必須パラメータ)
		templateEngineSrcFileName：EJSビルドの対象となるファイル名
		                               省略時は"ejs-1.0.js"
		modules                  ：コンフィグファイルにて指定されるモジュール名。constructionFileNameにて指定したファイルにてモジュール定義した名前をカンマ区切りで記載
		                               省略時は"scopedglobals", "util", "async", "resource", "controller", "dataModel","modelWithBinding", "view", "ui", "api.geo", "api.sqldb", "api.storage", "scene", "validation", "dev" 
		gitCommitId              ：gitのコミットID
		                           省略時はコミットID情報付与しません。


