package gitrpg;

import org.bson.Document;

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
		Main.main();
	}

	public static int githubCommitsGet(String url,String NAME,int DAY) throws Exception {
		url = "https://api.github.com/repos/" + url +"/commits";
		String reply=http.apiGet(url);

		String a = "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z\"},\"committer\":";
		String b = "\"},\"committer\":";
		reply = reply.replaceAll(a,b);
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("GithubCommit1");
		MongoCollection<Document> col2 = database.getCollection("GithubCommit2");
		MongoCollection<Document> col3 = database.getCollection("GithubCommit3");
		Mongo.mongoDelete(col1,col2,col3);

		Mongo.mongoSet1(col1, reply);

		Mongo.mongoFltr(col1,col2,"commit.author.name",NAME);
		Mongo.mongoTime(col2,col3,"commit.author.date",DAY);
		long count = col3.count();

		mongoClient.close();


		return (int) count;
	}



	public static String[] shaGet(MongoCollection<Document> col,int count) throws Exception {
		String sha[]=Mongo.mongoExtract(Mongo.mongoConvString(col),(int)count);

		for(int i=0;i<(int)count;i++){
			System.out.println(sha[i]);
		}

		return sha;
	}


}