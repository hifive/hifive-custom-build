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
					tmpFiles="false"
					templateEngineName="ejs-h5mod.js"
					cssName="h5.css"
					releaseName="h5.js"
					devName="h5.dev.js"
					jsDir="src/main/webapp/src"
					cssDir="src/main/webapp/srcCss"
					templateEngineSrcDir="src/main/webapp/src/ejs"
					dstDir="src/main/webapp/archives/current"
					version="1.0"
					templateEngineVersion="1.0"
					configBaseDir="./config/build"
					jsTemplateFile="template/hifive-ui-js.vm"
					cssTemplateFile="template/hifive-ui-css.vm"
					minHeaderFile="template/min-header.vm"
					constructionFile=h5-file-construction.xml
					moduleNames="util,async,resource,controller,dataModel,modelWithBinding,view,ui,api.geo,api.sqldb,api.storage,scene,validation" />
				<velocityParameter name="gitCommitId" value="ebce084e74e00017807dab63d358597aef6d26e4" />
			</buildService>


	- パラメータ一覧
		tmpFiles             ：tmpファイルを作るか否か(true,falseにて指定)
		templateEngineName   ：ビルドで作成されるEJSファイル名
		cssName              ：ビルドで作成されるCSSファイル名
		releaseName          ：ビルドで作成されるminifyされたJSファイル名
		devName              ：ビルドで作成されるminifyする前のJSファイル名
		jsDir                ：ビルド元のJSファイルが格納されているディレクトリ
		cssDir               ：ビルド元のCSSファイルが格納されているディレクトリ
		templateEngineSrcDir ：ビルド元のEJSファイルが格納されているディレクトリ
		dstDir               ：ビルドで作成されるファイルの出力先ディレクトリ
		version              ：ビルドで作成されるファイル名に含まれる版数(JSファイル用)
		templateEngineVersion：ビルドで作成されるファイル名に含まれる版数(EJS用)
		configBaseDir        ：コンフィグファイルが格納されているディレクトリ
		jsTemplateFile       ：JSファイルのテンプレートファイル名
		cssTemplateFile      ：CSSファイルのテンプレートファイル名
		minHeaderFile        ：ビルドで作成されるファイルのヘッダテンプレートファイル名
		constructionFile     ：コンフィグファイル名
		moduleNames          ：コンフィグファイルにて指定されるモジュール名。constructionFileにて指定したファイル()にてモジュール定義した名前をカンマ区切りで記載
		gitCommitId          ：gitのコミットID


