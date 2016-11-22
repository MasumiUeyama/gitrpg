package gitrpg;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * DWRでJSから呼ばれるメソッドはすべてpublicでなければならない．また，必要なクラスはすべてdwr.xmlに定義されている必要がある．
 * @author Hiroshi
 *
 */
public class Get {

	public static void main(String[] args) throws Exception{

	}

	public static int githubCommitsGet(String url,String NAME,int DAY) throws Exception {
		url = "https://api.github.com/repos/igakilab/" + url +"/commits";
		String reply=http.apiGet(url);

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

		Mongo.mongoDelete(col1,col2,col3);

		for(Object obj0 : json){
			JSONObject tmp = (JSONObject)obj0;
			Document doc1 = Document.parse(tmp.toJSONString());
			col1.insertOne(doc1);
		}

		Mongo.mongoFind(col1,col2,"commit.author.name",NAME);
		Mongo.mongoTime(col2,col3,"commit.author.date",DAY);
		long count = col3.count();
		mongoClient.close();
		return (int) count;
	}

}