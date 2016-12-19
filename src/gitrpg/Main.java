package gitrpg;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {

	static String team = "igakilab";
	static String repo = "gitrpg";
	static String name ="MasumiUeyama";
	static int day = 30;

	public static void main() throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Commit1");
		MongoCollection<Document> col2 = database.getCollection("Commit2");
		MongoCollection<Document> col3 = database.getCollection("Commit3");
		MongoCollection<Document> col4 = database.getCollection("changeGet1");
		MongoCollection<Document> col5 = database.getCollection("commentGet1");
		Mongo.mongoDelete(col1,col2,col3);
		System.out.println("コミット数:"+Get.CommitsGet(team,repo,name,day,col1,col2,col3));

		String sha[] = Get.shaGet(col3,Mongo.mongoCount(col3));

		//Get.changeGet(team,repo,sha,col4);
		Get.commentGet(team,repo,col4);
		mongoClient.close();
	}
}
