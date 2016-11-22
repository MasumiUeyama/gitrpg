package gitrpg;

import java.util.Calendar;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * DWRでJSから呼ばれるメソッドはすべてpublicでなければならない．また，必要なクラスはすべてdwr.xmlに定義されている必要がある．
 * @author Hiroshi
 *
 */
public class Mongo {

	public static void main(String[] args) throws Exception{
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

	public static void mongoFind(MongoCollection<Document> col1,MongoCollection<Document> col2,String key1,String key2) throws Exception {
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
}