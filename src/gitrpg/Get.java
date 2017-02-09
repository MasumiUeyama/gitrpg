package gitrpg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	public static void main(String[] args) throws Exception{
		Main.main();
	}

	public static String countResult(String name1,String name2) throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Result");
		int count = Mongo.mongoCount(col1);
		String strArray1[] = new String[count];
		int player1 = 0;
		int player2 = 0;
		int draw = 0;
		for(Document doc : col1.find()){
			JSONObject json = new JSONObject();
			json.put("win", doc.getString("win"));
			if(doc.getString("win").equals(name1)) player1++;
			if(doc.getString("win").equals(name2)) player2++;
			if(doc.getString("win").equals("draw")) draw++;
		}
		//System.out.println(result);
		String result =name1+"の勝利数"+player1+"　" +name2+"の勝利数"+ player2 +"　"+"引分け数"+ draw;
		return result;
	}

	public static String[] countMember() throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Member");
		int count = Mongo.mongoCount(col1);
		String strArray[] = new String[count];
		int n=0;
		for(Document doc : col1.find()){
			JSONObject json = new JSONObject();
			strArray[n]=(doc.getString("login"));
			n++;
		}
		//System.out.println(result);
		return strArray;
	}

	public static int countComment(String name) throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Comment");
		int result=0;
		for(Document doc : col1.find()){
			JSONObject json = new JSONObject();
			//json.put("id", doc.getString("id"));
			json.put("login", doc.getString("login"));
			if(doc.getString("login").equals(name)) result++;
			System.out.println(json.put("login", doc.getString("login")));
		}
		//System.out.println(result);
		return result;
	}

	public static int countCommit(String name) throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Commit");
		int result=0;
		for(Document doc : col1.find()){
			JSONObject json = new JSONObject();
			//json.put("id", doc.getString("id"));
			json.put("login", doc.getString("login"));
			if(doc.getString("login").equals(name)) result++;
			//System.out.println(doc.getString("login"));
		}
		//System.out.println(result);
		return result;
	}

	public static int countChange(String name) throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Commit");
		int sum=0;
		JSONArray result = new JSONArray();

		for(Document doc : col1.find()){
			//String json = doc.toJson();
			JSONObject json = new JSONObject();
			json.put("login", doc.getString("login"));
			if(doc.getString("login").equals(name)) sum+=doc.getInteger("change");
		}
		return  sum;
	}

	public static int countBranch(String name) throws Exception{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> col1 = database.getCollection("Branch");
		int sum=0;
		JSONArray result = new JSONArray();

		for(Document doc : col1.find()){
			//String json = doc.toJson();
			JSONObject json = new JSONObject();
			if(doc.getString("login").equals(name)) sum++;
			System.out.println(doc);
		}
		System.out.println("カウントブランチイイ"+sum);
		return  sum;
	}

	public static String getPhoto(String name) throws Exception{
		String url = "https://api.github.com/users/" + name;
		String reply="["+http.apiGet(url)+"]";
		String avatar ="";
		JSONParser p = new JSONParser();
		Object parsed = p.parse(reply);
		JSONArray array = (JSONArray)parsed;

		for(int i=0; i<array.size(); i++){
			JSONObject commit = (JSONObject)array.get(i);
			avatar = (String)commit.get("avatar_url");
		}
		return avatar;
	}

	public static void getComment(String TEAM,String REPOS,
			MongoCollection<Document> col1) throws Exception {
		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/comments";
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

			JSONObject t1;

			strArray[k] = (String)commit.get("created_at");
			int su=strArray[k].length();
			strArray[k] = strArray[k].substring(0,su-1);
			k++;

			t1 = (JSONObject)commit.get("user");
			strArray[k++] = (String)t1.get("login");

			strArray[k++] = (String)commit.get("body");

		}
		Mongo.deleteDatabase(col1);

		int m=0;

		for(int j=0; j<array.size(); j++){
			Document doc = new Document();
			doc.append("created_at",strArray[m++]);
			doc.append("login",strArray[m++]);
			doc.append("body",strArray[m++]);
			col1.insertOne(doc);
		}
	}



	public static void getBranch(
			MongoCollection<Document> col1,
			MongoCollection<Document> col2) throws Exception {

		JSONArray result = new JSONArray();

		for(Document doc : col1.find()){
			JSONObject json = new JSONObject();
			//json.put("id", doc.getString("id"));
			json.put("login", doc.getString("login"));
			json.put("branch", doc.getString("ref"));
			if(doc.getString("id").equals("CreateEvent")) result.add(json);
		}
		System.out.println("ブランチのテストっっっっっっっｐ"+result);
		Mongo.setDatabase1(col2, result.toJSONString());

	}

	public static void getEvent(String TEAM,String REPOS,int DAY,
			MongoCollection<Document> col1) throws Exception {

		String url="https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/events"+"?page=1";
		int page = 1;
		String reply=http.apiGet(url);
		String replys[] = new String[11];
		String next="";
		String end="[]";
		int su = 0;
		Calendar cal1 = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		cal1.add(Calendar.DATE, DAY*-1);

		page++;
		while(page!=10){
			url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/events"+"?page="+page;
			replys[page]=http.apiGet(url);
			if(replys[page].equals(end)) break;
			else if(page==2) {
				su=reply.length();
				reply= reply.substring(0,su-1);
			}
			su=replys[page].length();
			replys[page] = replys[page].substring(1,su-1);
			System.out.println(replys[page]);
			reply=reply+replys[page];
			page++;

		}
		if(page!=2) reply = reply + "]";
		//System.out.println(reply);
		//String a = "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z\"},\"committer\":";
		//String b = "\"},\"committer\":";
		//reply = reply.replaceAll(a,b);
		Mongo.setDatabase1(col1, reply);
		int count = Mongo.mongoCount(col1);
		String strArray[] = new String[count*4+1];

		JSONParser p = new JSONParser();
		Object parsed = p.parse(reply);
		JSONArray array = (JSONArray)parsed;

		int n=0;
		int k=0;
		for(int i=0; i<array.size(); i++){
			//JSONObjectにキャスト
			JSONObject commit = (JSONObject)array.get(i);

			JSONObject t1,t2,t3;
			String time = (String)commit.get("created_at");

			Date dtmp = df.parse(time);
			Calendar cal3 = Calendar.getInstance();
			cal3.setTime(dtmp);
			cal3.add(Calendar.HOUR, 9);
			time = df.format(cal3.getTime());
			strArray[k++] = time;

			su=strArray[k-1].length();
			//strArray[k-1] = strArray[k-1].substring(0,su-10);
			Date d = df.parse(strArray[k-1]);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(d);
			int diff = cal1.compareTo(cal2);
			strArray[k++] = (String)commit.get("type");
			t1 = (JSONObject)commit.get("actor");
			strArray[k++] = (String)t1.get("login");
			t1 = (JSONObject)commit.get("payload");
			strArray[k++] = (String)t1.get("ref");
			if(diff==1) break;
			n++;
		}
		Mongo.deleteDatabase(col1);

		int m=0;

		for(int j=0; j<n; j++){
			Document doc = new Document();
			doc.append("created_at",strArray[m++]);
			doc.append("id",strArray[m++]);
			doc.append("login",strArray[m++]);
			doc.append("ref",strArray[m++]);
			col1.insertOne(doc);
		}

	}
	public static List<CommitData> getCommit(String TEAM,String REPOS,int DAY,
			MongoCollection<Document> col1) throws Exception {

		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DATE, DAY*-1);
		String str = df.format(cal1.getTime());

		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/commits?since="+str;
//String url = "https://api.github.com/repos/igakilab/monsterzoo/commits";

		String reply=http.apiGet(url);
		//String a = "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z\"},\"committer\":";
		//String b = "\"},\"committer\":";
		//reply = reply.replaceAll(a,b);


		Mongo.setDatabase1(col1, reply);
		int count = Mongo.mongoCount(col1);
		String strArray[] = new String[count*5+1];

		JSONParser p = new JSONParser();
		Object parsed = p.parse(reply);

		//とってきたデータが配列のときはJSONArrayにキャストする
		JSONArray array = (JSONArray)parsed;

		int k=0;
		int n=0;
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
			String time = (String)t2.get("date");

			Date dtmp = df.parse(time);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(dtmp);
			cal2.add(Calendar.HOUR, 9);
			time = df.format(cal2.getTime());
			//System.out.println(time);
			strArray[k++] = time;

			String changeurl = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/commits/"+sha;

			strArray[k++] =String.valueOf(getChange2(changeurl));
			//strArray[k++] = "仮change";

			t1 = (JSONObject)commit.get("commit");
			strArray[k++] = (String)t1.get("message");
			n++;
		}
		Mongo.deleteDatabase(col1);

		int m=0;

		for(int j=0; j<n; j++){
			Document doc = new Document();
			doc.append("sha",strArray[m++]);
			doc.append("login",strArray[m++]);
			doc.append("date",strArray[m++]);
			doc.append("change",Integer.parseInt(strArray[m++]));
			doc.append("message",strArray[m++]);
			col1.insertOne(doc);
		}



		//List<CommitData> result = new ArrayList<>();

		/*			CommitData data = new CommitData();
		data.setName(doc.getString("name"));
		data.setChange(doc.getString("change"));*/


		JSONArray result = new JSONArray();

		//		for(Document doc : col1.find()){
		//			//String json = doc.toJson();
		//			JSONObject json = new JSONObject();
		//			json.put("name", doc.getString("login"));
		//			json.put("change", Integer.parseInt(doc.getString("change")));
		//			result.add(json);
		//
		//			System.out.println("名前: " + doc.getString("change"));
		//
		//		}
		//		System.out.println(result.toJSONString());
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
			long doc1= (Long)t1.get("additions");
			j=(int)doc1;
		}
		return j;
	}

	public static void getMember(String TEAM,String REPOS,MongoCollection<Document> col1) throws Exception{
		String url = "https://api.github.com/repos/" +TEAM+"/"+ REPOS +"/contributors";
		String reply=http.apiGet(url);
		Mongo.setDatabase1(col1, reply);
		int count = Mongo.mongoCount(col1);
		String strArray[] = new String[count*1+1];

		JSONParser p = new JSONParser();
		Object parsed = p.parse(reply);

		//とってきたデータが配列のときはJSONArrayにキャストする
		JSONArray array = (JSONArray)parsed;

		int k=0;
		int n=0;
		for(int i=0; i<array.size(); i++){
			//JSONObjectにキャスト
			JSONObject commit = (JSONObject)array.get(i);

			strArray[n] = (String)commit.get("login");
			System.out.println(strArray[n]);
			n++;
		}
		Mongo.deleteDatabase(col1);

		int m=0;

		for(int j=0; j<n; j++){
			Document doc = new Document();
			doc.append("login",strArray[m++]);
			col1.insertOne(doc);
		}
	}

	public static String helloWorld(String name){
		return name + ":HelloWorld";

	}
}