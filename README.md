開発環境の準備
#pleiades4.5の導入
* http://mergedoc.osdn.jp/ にアクセスし、「Eclipse4.5 Neon Pleiades All in One」を選択する．

* リストの中から「Java」の「Full Edition」を選択し、ファイルをダウンロードする．

* ダウンロードされた圧縮ファイルの「pleiades」フォルダをCドライブの直下に展開する．展開が完了したら「pleiades」フォルダの名前を「pleiades4.5」に変更する．下記ディレクトリ構成になるように展開する
 * c:\pleiades4.5\eclipse, java, tomcat
                       
* 環境変数を変更を行う．次の手順などでシステムの詳細設定の画面を開く．

 * PC(コンピュータを右クリック) -> プロパティ(又はコントロールパネルの「システム」)->システムの詳細設定->環境変数
 * スタートボタンをクリック -> 「システムの詳細設定を表示」と入力、開く -> 環境変数
 
* 環境変数に以下の値を設定する．（ユーザー環境変数/システム環境変数のどちらでもよい）

 * JAVA_HOME	C:\pleiades4.5\java\8
 * JRE_HOME	C:\pleiades4.5\java\8\jre

#MongoDBの導入
* https://www.mongodb.com/download-center　にアクセスする．
* 「Community Server」のタブを選択、「Windows」のタブを選択し、「Version」で「Windows Server 2008 R2 64-bit and later, with SSL support x64」を選択し、「DOWNLOAD」をクリックする．

* ダウンロードされたインストーラーを実行し，mongodbをインストールする．

インストールの種類(Complete/Custom)の選択を聞かれたら「Complete」を選択してください． 「Custom」を選択した場合でも、「Server」と「Client(shell)」がインストールされるように設定する．
* インストールが完了したら，Cドライブ直下に「db」という名前で空のフォルダを作成する．

#GCV導入
* https://github.com/igakilab/gitrpg よりClone

* Eclipseを起動しリポジトリをインポートする．

* build.xmlを右クリック->実行->Ant Build(2つ並んでるもののうえのほう）を選択．

* buildファイルに従って，コンパイルしてwarファイルが作成され，tomcatのwebappsディレクトリに配置される．
* tomcatのbinディレクトリ内のstartup.batを実行->tomcatが起動し，multiple-dwr.warが配備（デプロイ）される．

* MongoDBのbinディレクト(C:\Program Files\MongoDB\Server\3.2\bin)内のmongod.exeとmongo.exeを起動する．

#GCV実行
* tomcat及びMongoDBが正常に起動したのを確認後，(http://localhost:8080/gitrpg/first.html?user1=「自分のアカウント名」&repo=「チーム名-multiple」&team=igakilab&day=7)にアクセスする．

* 指定したアカウントのコミット数などが表示されることを確認する．

* 画面右部分の名前欄に同じリポジトリ内のアカウント名を入力し対戦ボタンをクリックし対戦画面にアクセスできることを確認する．

* 結果テキストをクリックし対戦結果が表示されることを確認する．
