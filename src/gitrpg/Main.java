package gitrpg;

import java.util.Random;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {

	static String team = "igakilab";
	//	static String repo = "tasks-monitor";
	//	static String name ="koike-marshmallow";
	//	static String name2 ="S-k-pt";
	static String repo = "gitrpg";
	static String name ="aoki-tha";
	static String name2 ="MasumiUeyama";
	static int day = 1134;

	public static int main() throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Comment");
		MongoCollection<Document> col2 = database.getCollection("Commit");
		MongoCollection<Document> col3 = database.getCollection("Event");
		MongoCollection<Document> col4 = database.getCollection("Branch");
		MongoCollection<Document> col5 = database.getCollection("Member");
		MongoCollection<Document> coltmp = database.getCollection("tmp");
		Mongo.deleteDatabase(col1,col2,col3);
		Mongo.deleteDatabase(col4,coltmp);

		//System.out.println(Get.countResult(name,name2));

//		Get.getMember(team,repo,col5);
//		Get.getComment(team,repo,col1);
//		Get.getCommit(team, repo,day, col2);
		Get.getEvent(team, repo,day,col3);
//		Get.getBranch(col3, col4);
//		String a=judge(name, name2);
//		System.out.println(a);
		mongoClient.close();
		int i=0;
		return i;
	}


	public static void inputDB(String team,String repo,int day) throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Comment");
		MongoCollection<Document> col2 = database.getCollection("Commit");
		MongoCollection<Document> col3 = database.getCollection("Event");
		MongoCollection<Document> col4 = database.getCollection("Branch");
		MongoCollection<Document> col5 = database.getCollection("Member");
		Mongo.deleteDatabase(col1,col2,col3);
		Mongo.deleteDatabase(col4,col5);
		Get.getMember(team,repo,col5);
		Get.getComment(team,repo,col1);
		Get.getCommit(team, repo,day, col2);
		Get.getEvent(team, repo,day,col3);
		Get.getBranch(col3, col4);
		mongoClient.close();
	}

	public static String judge(String name1,String name2) throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
//		MongoCollection<Document> col1 = database.getCollection("Comment");
//		MongoCollection<Document> col2 = database.getCollection("Commit");
//		MongoCollection<Document> col3 = database.getCollection("Event");
//		MongoCollection<Document> col4 = database.getCollection("Branch");
//		MongoCollection<Document> col5 = database.getCollection("Member");
//		MongoCollection<Document> coltmp = database.getCollection("tmp");
//		Mongo.deleteDatabase(col1,col2,col3);
//		Mongo.deleteDatabase(col4,col5,coltmp);
//		Get.getMember(team,repo,col5);
//		Get.getComment(team,repo,col1);
//		Get.getCommit(team, repo,day, col2);
//		Get.getEvent(team, repo,day,col3);
//		Get.getBranch(col3, col4);
		MongoCollection<Document> col6 = database.getCollection("Result");

		Random rnd = new Random();
		int p1=Get.countComment(name1)*50 + Get.countCommit(name1)*100 + Get.countChange(name1)+ Get.countBranch(name1)*50;
		int p2=Get.countComment(name2)*50 + Get.countCommit(name2)*100 + Get.countChange(name2)+ Get.countBranch(name2)*50;

		String result="";

		if(p1>p2){
			p1 = rnd.nextInt(6) + 3;
			p2 = rnd.nextInt(6) + 1;
		} else if(p1<p2){
			p1 = rnd.nextInt(6) + 1;
			p2 = rnd.nextInt(6) + 3;
		} else {
			p1 = rnd.nextInt(10) + 10;
			p2 = rnd.nextInt(10) + 10;
		}

		if(p1>p2)  result= name1+"の勝ち";
		if(p1<p2)  result= name2+"の勝ち";
		if(p1==p2)  result= "わけ";

		//System.out.println(p1+":"+p2);

		Document doc = new Document();

		if(p1>p2){
			doc.append("player",name1+"-vs-"+name2);
			doc.append("win",name1);
			col6.insertOne(doc);
		} else if(p1<p2){
			doc.append("player",name1+"-vs-"+name2);
			doc.append("win",name2);
			col6.insertOne(doc);
		} else {
			doc.append("player",name1+"-vs-"+name2);
			doc.append("win","draw");
			col6.insertOne(doc);
		}

		return result;

	}

	//	public static List<CommitData> main2() throws Exception{
	//		MongoClient mongoClient = new MongoClient();
	//		MongoDatabase database = mongoClient.getDatabase("mydb");
	//		MongoCollection<Document> col7 = database.getCollection("Commit");
	//
	//		return Get.getCommit(team, repo, name, day, col7);
	//	}

	public static int commit(String team,String repo,String name,int day,MongoCollection<Document>col1,MongoCollection<Document>col2,MongoCollection<Document>col3) throws Exception{
		int i =0;
		return i;
	}
}
