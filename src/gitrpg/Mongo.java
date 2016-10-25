package gitrpg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

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
		Mongo g = new Mongo();
		g.executeGet();
		//Mongo p = new Mongo();
		//p.executePost();
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
            URL url = new URL("https://api.github.com/repos/igakilab/gitrpg/commits");
            //URL url = new URL("https://api.github.com/repos/igakilab/ueyamatest/commits?access_token=28c2f93a824feabdb3ca049016c6c307ad979da1");
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

        String a = "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z\"},\"committer\":";
        String b = "\"},\"committer\":";

        System.out.println("ここまで");

        reply = reply.replaceAll(a,b);

        System.out.println(reply);
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("mydb");
        //Document doc = Document.parse(reply);
        MongoCollection<Document> col1 = database.getCollection("test1");
        MongoCollection<Document> col2 = database.getCollection("test2");
        MongoCollection<Document> col3 = database.getCollection("test3");
        //col.insertOne(doc);

        JSONParser parser = new JSONParser();
        //String s="[{id:1}, {id:2}]";

        Object obj = parser.parse(reply);
        JSONArray json = (JSONArray)obj;

        //リセット
        col1.deleteMany(new Document());
        col2.deleteMany(new Document());
        col3.deleteMany(new Document());

        for(Object obj0 : json){
        	JSONObject tmp = (JSONObject)obj0;
        	Document doc1 = Document.parse(tmp.toJSONString());
        	col1.insertOne(doc1);
        }

//        /**
//         *       for(int i=0;i<=count;i++){
//         *       	Document doc3 = new Document("commit.author.name", "MasumiUeyama");
//         *       	//col.deleteMany(doc3);
//         *       	//DeleteResult deleteResult = col.deleteMany(doc3); //System.out.println( "でりーと" +deleteResult.getDeletedCount());
//         *       	//System.out.println(deleteResult);
//         *       }
//         */


//        Document myDoc = col.find(in("commit.author.date",today)).first();
//        System.out.println(myDoc.toJson());

        long count = col1.count();
        System.out.println("cal1.全部指定:" + count);

		BasicDBObject query = new BasicDBObject();
		BasicDBObject query2 = new BasicDBObject();
		//query.put("commit.author.name", "Koike Takaaki");
		//query.put("commit.author.name", "kioke takaaki");
		query.put("commit.author.name", "MasumiUeyama");

		Document doc1;
		Document doc2;

		//Document time = col.find(in("commit.author.date",today)).first();

		MongoCursor<Document> cursor = col1.find(query).iterator();
		while (cursor.hasNext()) {
			doc1 = cursor.next();
	       	col2.insertOne(doc1);
		}

        count = col2.count();
        System.out.println("col2.名前指定:" + count);


        String time=a;
        Calendar cal = Calendar.getInstance();
        int DAY=7;
        for(int i =0;i<DAY;i++){
        	int year = cal.get(Calendar.YEAR);
        	int month = cal.get(Calendar.MONTH) + 1;
        	int day = cal.get(Calendar.DATE);
        	time =year + "-" + month  + "-" + day;
        	//System.out.println(time);
        	cal.add(Calendar.DATE, -1);

        	query2.put("commit.author.date", time);
        	MongoCursor<Document> cursor2 = col2.find(query2).iterator();
        	while (cursor2.hasNext()) {
        		doc2 = cursor2.next();
        		col3.insertOne(doc2);
        		//col2.insertOne(cursor.next());
        	}
        }
        count = col3.count();
        System.out.println("col3. "+DAY+"日間分:" + count);

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


    public void executePost() {
        System.out.println("===== HTTP POST Start =====");
        try {
            URL url = new URL("https://habitica.com/api/v3/challenges");

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),
                                                                                  StandardCharsets.UTF_8));
                writer.write("POST Body");
                writer.write("\r\n");
                writer.write("Hello Http Server!!");
                writer.write("\r\n");
                writer.flush();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                                                                       StandardCharsets.UTF_8);
                         BufferedReader reader = new BufferedReader(isr)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
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

        System.out.println("===== HTTP POST End =====");
    }
}