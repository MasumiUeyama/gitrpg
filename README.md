開発環境の準備
#pleiades4.5のダウンロード
#MongoDBのインストール

#pleiades4.5のダウンロード
(1) http://mergedoc.osdn.jp/にアクセスし、「Eclipse 4.5 Neon Pleiades All in One」を選択します。

(2) リストの中から「Java」の「Full Edition」を選択し、ファイルをダウンロードします。

(3) ダウンロードされた圧縮ファイルの「pleiades」フォルダをCドライブの直下に展開します。展開が完了したら「pleiades」フォルダの名前を「pleiades4.5」に変更します。以下のようなディレクトリ構造になっていることを確認してください

C: --|-- pleiades4.5 --|-- java
     |-- ...           |-- eclipse
                       |-- tomcat ...
(4) 環境変数を変更します。次の手順などでシステムの詳細設定の画面を開きます。

例: PC(コンピュータを右クリック) -> プロパティ(又はコントロールパネルの「システム」)->システムの詳細設定->環境変数
例: スタートボタンをクリック -> 「システムの詳細設定を表示」と入力、開く -> 環境変数
(5) 環境変数に以下の値を設定します。（ユーザー環境変数/システム環境変数のどちらでもよい）

変数名	変数値
JAVA_HOME	C:\pleiades4.5\java\8
JRE_HOME	C:\pleiades4.5\java\8\jre

#MongoDBのインストール
(1) https://www.mongodb.com/download-centerにアクセスします。

(2) 「Community Server」のタブを選択、「Windows」のタブを選択し、「Version」で「Windows Server 2008 R2 64-bit and later, with SSL support x64」を選択し、「DOWNLOAD」をクリックします。

(3) ダウンロードされたインストーラーを実行し、mongodbをインストールします。

インストールの種類(Complete/Custom)の選択を聞かれたら「Complete」を選択してください。 「Custom」を選択した場合でも、「Server」と「Client(shell)」がインストールされるように設定してください。
(4) インストールが完了したら、Cドライブ直下に「db」という名前で空のフォルダを作成しておきます。

C: --|-- db
     |-- ...
