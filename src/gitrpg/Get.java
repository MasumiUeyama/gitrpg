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
public class Get {

	public static void main(String[] args) throws Exception{
		Mongo p = new Mongo();
		p.githubCommitsGet("gitrpg");
	}

    public static int githubCommitsGet(String url) throws Exception {
    	url = "https://api.github.com/repos/igakilab/" + url +"/commits";
		String reply=http.githubGet(url);

        String a = "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z\"},\"committer\":";
        String b = "\"},\"committer\":";
        reply = reply.replaceAll(a,b);

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("mydb");
        MongoCollection<Document> col1 = database.getCollection("GithubCommit1");
        MongoCollection<Document> col2 = database.getCollection("GithubCommit2");
        MongoCollection<Document> col3 = database.getCollection("GithubCommit3");

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(reply);
        JSONArray json = (JSONArray)obj;

        col1.deleteMany(new Document());
        col2.deleteMany(new Document());
        col3.deleteMany(new Document());

        for(Object obj0 : json){
        	JSONObject tmp = (JSONObject)obj0;
        	Document doc1 = Document.parse(tmp.toJSONString());
        	col1.insertOne(doc1);
        }

		BasicDBObject query = new BasicDBObject();
		BasicDBObject query2 = new BasicDBObject();
		query.put("commit.author.name", "MasumiUeyama");

		Document doc1;
		Document doc2;

		MongoCursor<Document> cursor = col1.find(query).iterator();
		while (cursor.hasNext()) {
			doc1 = cursor.next();
	       	col2.insertOne(doc1);
		}

        String time=a;
        Calendar cal = Calendar.getInstance();
        int DAY=7;
        for(int i =0;i<DAY;i++){
        	int year = cal.get(Calendar.YEAR);
        	int month = cal.get(Calendar.MONTH) + 1;
        	int day = cal.get(Calendar.DATE);
        	time =year + "-" + month  + "-" + day;
        	cal.add(Calendar.DATE, -1);

        	query2.put("commit.author.date", time);
        	MongoCursor<Document> cursor2 = col2.find(query2).iterator();
        	while (cursor2.hasNext()) {
        		doc2 = cursor2.next();
        		col3.insertOne(doc2);
        	}
        }
        long count = col3.count();
        mongoClient.close();

        return (int) count;
    }





    public void executePost() {
        System.out.println("===== HTTP POST Start =====");
        try {
            URL url = new URL("https://habitica.com/api/v3/tasks/user");

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("x-api-user", "ea2f74bd-5080-4345-a95d-842455aeb4f1");
                connection.setRequestProperty("x-api-key", "b3f10b82-6937-4e79-aaae-82babe1ec2df");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),
                                                                                  StandardCharsets.UTF_8));

                writer.write("text=commit&type=habit&userId=ea2f74bd-5080-4345-a95d-842455aeb4f1&notes=test");
                //writer.write("text=ペニ&type=todo&userId=ea2f74bd-5080-4345-a95d-842455aeb4f1");

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