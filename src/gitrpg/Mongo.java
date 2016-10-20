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

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
     * httpクライアント
     * http://d.hatena.ne.jp/Kazuhira/20131026/1382796711
     *
     * Quick Tour
     * http://mongodb.github.io/mongo-java-driver/3.3/driver/getting-started/quick-tour/
     *
     * json sample
     * http://www.canetrash.jp/article/170576378.html

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
            //URL url = new URL("https://api.github.com/repos/masumiueyama/gitrpg/commits");
            URL url = new URL("https://api.github.com/repos/igakilab/ueyamatest/commits?access_token=28c2f93a824feabdb3ca049016c6c307ad979da1");
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
        MongoCollection<Document> col2 = database.getCollection("test2");
        //col.insertOne(doc);

        JSONParser parser = new JSONParser();
        //String s="[{id:1}, {id:2}]";

        Object obj = parser.parse(reply);
        JSONArray json = (JSONArray)obj;

        //リセット
        col.deleteMany(new Document());
        col2.deleteMany(new Document());

        for(Object obj0 : json){
        	JSONObject tmp = (JSONObject)obj0;
        	Document doc1 = Document.parse(tmp.toJSONString());
        	col.insertOne(doc1);
        }


/**
        for(int i=0;i<=count;i++){
        	Document doc3 = new Document("commit.author.name", "MasumiUeyama");
        	//col.deleteMany(doc3);
        	//DeleteResult deleteResult = col.deleteMany(doc3); //System.out.println( "でりーと" +deleteResult.getDeletedCount());
        	//System.out.println(deleteResult);
        }
**/


        long count = col.count();
        System.out.println("All:" + count);

		BasicDBObject query = new BasicDBObject();
		//query.put("commit.author.name", "Koike Takaaki");
		//query.put("commit.author.name", "kioke takaaki");
		query.put("commit.author.name", "ue");
		System.out.println(query);
		Document doc4;

		MongoCursor<Document> cursor = col.find(query).iterator();
		while (cursor.hasNext()) {
			doc4 = cursor.next();
	       	col2.insertOne(doc4);
			//col2.insertOne(cursor.next());
		}

        count = col2.count();
        System.out.println("col2.koike-marshmallow:" + count);

        /**
		MongoCursor<Document> cursor = col.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }


        count = col.count();

        for(int i=0;i<=count;i++){
        	Document doc3 = new Document("commit.author.name", "ue");
        	col.deleteMany(doc3);
        }


        ちゃんと動くやつ
        col.deleteMany(new Document("sha", "01641de3f3b18b2f307c9067d68faba122fe4953"));
        動かなe
    	doc3.append("commit", new Document("author", new Document("date", "2016-08-02T09:27:33Z")));
        **/

        mongoClient.close();

        //ctrl+alt+x -> j


        return buffer.toString();
    }
}