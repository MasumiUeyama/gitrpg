package gitrpg;

import java.util.List;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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

	public static List<CommitData> getCommit2(String TEAM,String REPOS,String NAME,int DAY,
			MongoCollection<Document> col1) throws Exception {
		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/commits";
		String reply=http.apiGet(url);
		String a = "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z\"},\"committer\":";
		String b = "\"},\"committer\":";
		reply = reply.replaceAll(a,b);

		Mongo.setDatabase1(col1, reply);
		int count = Mongo.mongoCount(col1);
		String strArray[] = new String[count*5+1];

		JSONParser p = new JSONParser();
		Object parsed = p.parse(reply);

		//とってきたデータが配列のときはJSONArrayにキャストする
		JSONArray array = (JSONArray)parsed;

		int k=0;
		for(int i=0; i<array.size(); i++){
		    //JSONObjectにキャスト
		    JSONObject commit = (JSONObject)array.get(i);

		    JSONObject t1,t2,t3;

		    strArray[k++] = (String)commit.get("sha");
		    String sha=strArray[k-1];
		    t1 = (JSONObject)commit.get("commit");
		    t2 = (JSONObject)t1.get("author");
		    strArray[k++] = (String)t2.get("name");

		    t1 = (JSONObject)commit.get("commit");
		    t2 = (JSONObject)t1.get("author");
		    strArray[k++] = (String)t2.get("date");

		    String changeurl = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/commits/"+sha;

		    strArray[k++] =String.valueOf(getChange2(changeurl));
		    //strArray[k++] = "仮change";

		    t1 = (JSONObject)commit.get("commit");
		    strArray[k++] = (String)t1.get("message");
		}
		Mongo.deleteDatabase(col1);

		int m=0;

		for(int j=0; j<array.size(); j++){
			Document doc = new Document();
			doc.append("sha",strArray[m++]);
			doc.append("name",strArray[m++]);
			doc.append("date",strArray[m++]);
			doc.append("change",strArray[m++]);
			doc.append("message",strArray[m++]);
			col1.insertOne(doc);

		}

		//List<CommitData> result = new ArrayList<>();

		/*			CommitData data = new CommitData();
		data.setName(doc.getString("name"));
		data.setChange(doc.getString("change"));*/


		JSONArray result = new JSONArray();

		for(Document doc : col1.find()){
			//String json = doc.toJson();
			JSONObject json = new JSONObject();
			json.put("name", doc.getString("name"));
			json.put("change", Integer.parseInt(doc.getString("change")));
			result.add(json);

			System.out.println("名前: " + doc.getString("change"));

		}
		System.out.println(result.toJSONString());
		return  result;
	}


	public static int getChange2(String url) throws Exception {
		int j=0;
		String reply="["+http.apiGet(url)+"]";
		JSONParser p = new JSONParser();
		Object parsed = p.parse(reply);
		JSONArray array = (JSONArray)parsed;
		for(int i=0; i<array.size(); i++){
		    //JSONObjectにキャスト
		    JSONObject commit = (JSONObject)array.get(i);

		    //shaを取り出し

		    //JSONObject t1 = (JSONObject)commit.get("commit");
		    //JSONObject t2 = (JSONObject)t1.get("author");
		    //strArray[i] = (String)t2.get("name");
		    //System.out.println(strArray[i]);

		    //[]のやつのときはJson
		    JSONObject t1 = (JSONObject)commit.get("stats");
		    long doc1= (Long)t1.get("total");
		    j=(int)doc1;
		}
		return j;
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