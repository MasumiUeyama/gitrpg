package gitrpg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
/**
 * DWRでJSから呼ばれるメソッドはすべてpublicでなければならない．また，必要なクラスはすべてdwr.xmlに定義されている必要がある．
 * @author Hiroshi
 *
 */
public class Mongo {

	public static void main(String[] args)
	throws Exception{
		Mongo m = new Mongo();
		m.executeGet();
	}

    /**
     * 参考:http://d.hatena.ne.jp/Kazuhira/20131026/1382796711
     * https://translate.google.co.jp/translate?sl=en&tl=ja&js=y&prev=_t&hl=ja&ie=UTF-8&u=http%3A%2F%2Fmongodb.github.io%2Fmongo-java-driver%2F2.13%2Fgetting-started%2Fquick-tour%2F&edit-text=
     * 正直これ見たかどうか覚えてないけどぐぐったらコレでた
     * @return
     * @throws ParseException
     * @throws Exception
     */

    public  String executeGet() throws ParseException {
    	//文字列buffer作ったよ
    	StringBuffer buffer = new StringBuffer();
    	//しばらくjson見れたかどうかのチェック？
        try {
            //URL url = new URL("https://api.github.com/repos/igakilab/api/commits?page=3&per_page=50");
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
        String reply = buffer.toString();
        System.out.println(reply);
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("mydb");
        //Document doc = Document.parse(reply);
        MongoCollection<Document> col = database.getCollection("test");
        //col.insertOne(doc);

        JSONParser parser = new JSONParser();
        //String s="[{id:1}, {id:2}]";

        Object obj = parser.parse(reply);
        JSONArray json = (JSONArray)obj;
        col.deleteMany(new Document());
        for(Object obj0 : json){
        	JSONObject tmp = (JSONObject)obj0;
        	Document doc1 = Document.parse(tmp.toJSONString());
        	doc1.append("msg", "井垣先生、無条件で単位をください");
        	col.insertOne(doc1);
        }


        Document doc3 = new Document("commit.author.date", "2016-08-02T09:27:33Z");
        //doc3.append("commit", new Document("author", new Document("date", "2016-08-02T09:27:33Z")));
        col.deleteMany(doc3);

        //ちゃんと動くやつ
        //col.deleteMany(new Document("sha", "01641de3f3b18b2f307c9067d68faba122fe4953"));

        long count = col.count();
        System.out.println(count + "件やでえええええええcaえa");
        mongoClient.close();

        //col.deleteMany(new Document("sha", "01641de3f3b18b2f307c9067d68faba122fe4953"));
        
        //ctrl+alt+x -> j


        return buffer.toString();
    }
}