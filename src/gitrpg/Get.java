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

	public static int getCommit(String TEAM,String REPOS,String NAME,int DAY,
			MongoCollection<Document> col1,
			MongoCollection<Document> col2,
			MongoCollection<Document> col3) throws Exception {
		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/commits";
		String reply=http.apiGet(url);
		String a = "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z\"},\"committer\":";
		String b = "\"},\"committer\":";
		reply = reply.replaceAll(a,b);
		Mongo.setDatabase1(col1, reply);
		int count2 = Mongo.mongoCount(col1);
		System.out.println(reply);
		System.out.println(count2);
		Mongo.fltrDatabase(col1,col2,"commit.author.name",NAME);
		Mongo.getTime(col2,col3,"commit.author.date",DAY);
		int count = Mongo.mongoCount(col3);

		return  count;
	}



	public static String[] getSha(MongoCollection<Document> col,int count) throws Exception {
		String sha[]=Mongo.extractSha(Mongo.convString(col),(int)count);
		for(int i=0;i<(int)count;i++){
			//System.out.println(sha[i]);
		}

		return sha;
	}

	public static void getChange(String TEAM,String REPOS,String sha[],
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
		Mongo.setDatabase1(col1, reply);
		int count = Mongo.mongoCount(col1);
		System.out.println("数値:"+count);
		int intArray[] = new int[count+1];
		intArray = Mongo.extractInt(reply, count,"Change");
		for(int i=0;i<count;i++) System.out.println(intArray[i]);

	}

	public static void getComment(String TEAM,String REPOS,
			MongoCollection<Document> col1) throws Exception{
		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/comments";
		String reply=http.apiGet(url);
		Mongo.setDatabase1(col1, reply);
		int count = Mongo.mongoCount(col1);
		String strArray[] = new String[count*3];
		strArray =Mongo.extractStr(reply, count,"Comment");
		for(int i=0;i<count*3;i++) System.out.println("こめのｔ"+strArray[i]);

	}

	public static void getMember(String TEAM,String REPOS,
			MongoCollection<Document> col1) throws Exception{
		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/contributors";
		String reply=http.apiGet(url);
		Mongo.setDatabase1(col1, reply);
		int count = Mongo.mongoCount(col1);
		String strArray[] = new String[count*3+3];
		strArray =Mongo.extractStr(reply, count,"Member");
		for(int i=0;i<count;i++) System.out.println("メンバ"+strArray[i]);;

	}

    public static String helloWorld(String name){
    	return name + ":HelloWorld";

    }
}