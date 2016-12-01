package gitrpg;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

class MonboTest{
	public static void main(String[] args){
		MongoClient client = new MongoClient("150.89.234.253", 27018);

		Monbo mon = new Monbo(client);

		mon.allRemove("gittasks", "ueyama");
	}
}

public class Monbo {
	private MongoClient client;

	public Monbo(MongoClient client){
		this.client = client;
	}

	public void allRemove(String dbName, String colName){
		MongoCollection<Document> col = client.getDatabase(dbName).getCollection(colName);

		col.deleteMany(new Document());
	}
}
