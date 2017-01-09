package gitrpg;

import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {

	static String team = "igakilab";
	static String repo = "gitrpg";
	static String name ="MasumiUeyama";
	static int day = 2;

	public static int main() throws Exception{


		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Commit1");
		MongoCollection<Document> col2 = database.getCollection("Commit2");
		MongoCollection<Document> col3 = database.getCollection("Commit3");
		MongoCollection<Document> col4 = database.getCollection("changeGet1");
		MongoCollection<Document> col5 = database.getCollection("commentGet1");
		MongoCollection<Document> col6 = database.getCollection("commentGet1");
		MongoCollection<Document> col7 = database.getCollection("Commit");
		MongoCollection<Document> col8 = database.getCollection("Comment");
		Mongo.deleteDatabase(col1,col2,col3);
		Mongo.deleteDatabase(col4,col5,col6);
		Mongo.deleteDatabase(col7,col8);
		//System.out.println("コミット数:"+Get.getCommit(team,repo,name,day,col1,col2,col3));

		//String sha[] = Get.getSha(col3,Mongo.mongoCount(col3));

		//Get.getChange(team,repo,sha,col4);
		//Get.getComment(team,repo,col5);
		//Get.getMember(team,repo,col6);
		//int i = Get.getCommit(team,repo,name,day,col1,col2,col3);
		//System.out.println(i);

		String a = Get.getPhoto("MasumiUeyama");
		System.out.println(a);
		Get.getCommit2(team, repo,name,day, col7);
		mongoClient.close();
		int i=0;
		return i;
	}


	public static List<CommitData> main2() throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col7 = database.getCollection("Commit");

		return Get.getCommit2(team, repo, name, day, col7);
	}

	public static int commit(String team,String repo,String name,int day,MongoCollection<Document>col1,MongoCollection<Document>col2,MongoCollection<Document>col3) throws Exception{
		int i =0;
		return i;
	}
}
