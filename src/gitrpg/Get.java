package gitrpg;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

/**
 * DWRでJSから呼ばれるメソッドはすべてpublicでなければならない．また，必要なクラスはすべてdwr.xmlに定義されている必要がある．
 * @author Hiroshi
 *
 */
public class Get {

	public static void main(String[] args) throws Exception{
		Main.main();
	}

	public static int CommitsGet(String TEAM,String REPOS,String NAME,int DAY,
			MongoCollection<Document> col1,
			MongoCollection<Document> col2,
			MongoCollection<Document> col3) throws Exception {
		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/commits";
		String reply=http.apiGet(url);

		String a = "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z\"},\"committer\":";
		String b = "\"},\"committer\":";
		reply = reply.replaceAll(a,b);
		Mongo.mongoSet1(col1, reply);

		Mongo.mongoFltr(col1,col2,"commit.author.name",NAME);
		Mongo.mongoTime(col2,col3,"commit.author.date",DAY);
		int count = Mongo.mongoCount(col3);

		return  count;
	}



	public static String[] shaGet(MongoCollection<Document> col,int count) throws Exception {
		String sha[]=Mongo.mongoShaExtract(Mongo.mongoConvString(col),(int)count);
		for(int i=0;i<(int)count;i++){
			//System.out.println(sha[i]);
		}

		return sha;
	}

	public static void changeGet(String TEAM,String REPOS,String sha[],
			MongoCollection<Document> col1) throws Exception {

		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/commits";
		String reply="[";
		String urls[]=new String[sha.length];
		for(int i=0;i<sha.length-1;i++){
			urls[i]=url+"/" + sha[i];
			reply = reply+ http.apiGet(urls[i]);
			//System.out.println(urls[i]);
		}
		reply = reply + "]";
		//System.out.println(reply);
		Mongo.mongoSet1(col1, reply);
		int count = Mongo.mongoCount(col1);
		Mongo.test(reply, count);

	}

	public static void commentGet(String TEAM,String REPOS,
			MongoCollection<Document> col1) throws Exception{
		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/comments";
		String reply=http.apiGet(url);
		Mongo.mongoSet1(col1, reply);
		int count = Mongo.mongoCount(col1);
		Mongo.test2(reply, count);

	}
}