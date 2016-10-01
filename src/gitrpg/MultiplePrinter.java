package gitrpg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * DWRでJSから呼ばれるメソッドはすべてpublicでなければならない．また，必要なクラスはすべてdwr.xmlに定義されている必要がある．
 * @author Hiroshi
 *
 */
public class MultiplePrinter {

//    public static void main(String[] args) throws InvalidValueException {
//        CloudSpiralPrinter csp = new CloudSpiralPrinter();
//        CSPInput input = new CSPInput();
//        List<String> list = csp.execute(input);
//        System.out.println(list);
//    }


    /**
     * 参考:http://d.hatena.ne.jp/Kazuhira/20131026/1382796711
     * 正直これ見たかどうか覚えてないけどぐぐったらコレでた
     * @return
     * @throws Exception
     */

    public  String executeGet() {
    	//文字列buffer作ったよ
    	StringBuffer buffer = new StringBuffer();
    	//しばらくjson見れたかどうかのチェック？
        try {
            URL url = new URL("https://api.github.com/repos/igakilab/api/commits");

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                                                                       StandardCharsets.UTF_8);
                         BufferedReader reader = new BufferedReader(isr)) {
                        //文末まで読んで全部引っ付ける
                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    /**
     * 参考:http://gootara.org/library/2014/04/javaapijsonjdk1618.html
     * @return
     * @throws Exception
     */

    public String executeParseJson() throws Exception{
    	String data = executeGet();

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        // ScriptEngine の eval に JSON を渡す時は、括弧で囲まないと例外が発生します。eval はセキュリティ的には好ましくないので、安全であることが不明なデータを扱うことは想定していません。
        // 外部ネットワークと連携するプログラムで使用しないでください。
        Object obj = engine.eval(String.format("(%s)", data));
        // Rhino は、jdk1.6,7までの JavaScript エンジン。jdk1.8は「jdk.nashorn.api.scripting.NashornScriptEngine」
        Map<String, Object> map = JsonSample.jsonToMap(obj, engine.getClass().getName().equals("com.sun.script.javascript.RhinoScriptEngine"));
        return map.toString();
    }




    /**
     * 1から10までのすべての数値について，
     * 以下の条件を満たしながら小さい順にリスト化して返す
     *   条件1：数値が3の倍数のときは”ryokun”をリストに追加する．
     * @return 1から10までの数値を変換した文字列のリスト
     */
    public List<String> execute(MultipleForm input) throws InvalidValueException {
        List<String> list = new ArrayList<>();
        int max = input.getMax();
        int multiple = input.getMultiple();


        if (multiple < 0 || max < 0){
        	throw new InvalidValueException("倍数は 正の整数(>0)でなければいけません．現在の値：" + multiple);
        }
        for(int i= 1; i<=max; i++){
        	if(i % multiple == 0){
        		list.add("ryokun");
        	}else{
        		list.add(Integer.toString(i));
        	}
        }

        return list;
    }

    /**
     * REST呼び出しを想定．
     * http://sample.com:8080/project_name/dwr/jsonp/ClassName/MethodName/param1/param2/ と指定する
     * 呼び出し例： http://localhost:8080/multiple-dwr/dwr/jsonp/MultiplePrinter/executeJson/ryokun/3/
     * @param msg multipleで指定された倍数のときに入れ替える文字列
     * @param multiple 倍数 (3の倍数とか5の倍数を指定する） >0 であるかをチェックし例外を投げる
     * @return multipleで指定された倍数の数字をmsgと入れ替えた1~10までのリストを返す
     * @throws InvalidValueException
     */
    public List<String> executeJson(String msg, int multiple) throws InvalidValueException {
        List<String> list = new ArrayList<>();


        if (multiple < 0){
        	throw new InvalidValueException("倍数は 正の整数(>0)でなければいけません．現在の値：" + multiple);
        }
        for(int i= 1; i<=10; i++){
        	if(i % multiple == 0){
        		list.add(msg);
        	}else{
        		list.add(Integer.toString(i));
        	}
        }
		return list;


    }
}
