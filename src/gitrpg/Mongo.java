package gitrpg;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * DWRでJSから呼ばれるメソッドはすべてpublicでなければならない．また，必要なクラスはすべてdwr.xmlに定義されている必要がある．
 * @author Hiroshi
 *
 */
public class Mongo {

	public static void main(String[] args) throws Exception{
		Main.main();
	}

	public static void mongoSet1(MongoCollection<Document> col1,String reply) throws Exception {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(reply);
		JSONArray json = (JSONArray)obj;

		for(Object obj0 : json){
			JSONObject tmp = (JSONObject)obj0;
			Document doc1 = Document.parse(tmp.toJSONString());
			col1.insertOne(doc1);
		}
	}

	public static void mongoDelete(MongoCollection<Document> col1) throws Exception {
		col1.deleteMany(new Document());
	}

	public static void mongoDelete(MongoCollection<Document> col1,MongoCollection<Document> col2) throws Exception {
		col1.deleteMany(new Document());
		col2.deleteMany(new Document());
	}

	public static void mongoDelete(MongoCollection<Document> col1,MongoCollection<Document> col2,MongoCollection<Document> col3) throws Exception {
		col1.deleteMany(new Document());
		col2.deleteMany(new Document());
		col3.deleteMany(new Document());
	}

	public static void mongoFltr(MongoCollection<Document> col1,MongoCollection<Document> col2,String key1,String key2) throws Exception {
		BasicDBObject query = new BasicDBObject();
		Document doc1;
		query.put(key1, key2);
		MongoCursor<Document> cursor = col1.find(query).iterator();
		while (cursor.hasNext()) {
			doc1 = cursor.next();
			col2.insertOne(doc1);
		}
	}

	public static void mongoTime(MongoCollection<Document> col1,MongoCollection<Document> col2,String key,int DAY) throws Exception {
		BasicDBObject query = new BasicDBObject();
		Document doc1;
		Calendar cal = Calendar.getInstance();
		for(int i =0;i<=DAY;i++){
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DATE);
			String time =year + "-" + month  + "-" + day;
			cal.add(Calendar.DATE, -1);
			query.put(key, time);
			MongoCursor<Document> cursor1 = col1.find(query).iterator();
			while (cursor1.hasNext()) {
				doc1 = cursor1.next();
				col2.insertOne(doc1);
			}
		}
	}

	public static String mongoConvString(MongoCollection<Document> col1) throws Exception {
		String doc="a";
		FindIterable<Document> iterator = col1.find();
		MongoCursor<Document> cursor = iterator.iterator();
		while(cursor.hasNext()){
			doc += cursor.next();
			doc += System.getProperty("line.separator");
		}

		return doc;
	}


	public static String[] mongoExtract(String doc,int i) throws Exception {
		int count=0;
		String strArray[] = new String[i+1];
		String regex = "comments, sha=........................................";
		Pattern pa = Pattern.compile(regex);
		Matcher m1 = pa.matcher(doc);
		if (m1.find() ) {
			do {
				strArray[count++]=m1.group().substring(14, 54);
			} while (m1.find() );
			System.out.println();
		}
		return strArray;
	}
}