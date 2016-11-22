package gitrpg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class http {

	public static String apiGet(String args) throws Exception{
		//文字列buffer作ったよ
		StringBuffer buffer = new StringBuffer();
		//しばらくjson見れたかどうかのチェック？
		try {
			URL url = new URL(args);
			HttpURLConnection connection = null;

			try {
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");

				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
							StandardCharsets.UTF_8);
							BufferedReader reader = new BufferedReader(isr)) {
						//文末まで読んで全部引っ付ける
						String line;
						while ((line = reader.readLine()) != null) {
							buffer.append(line);
						}
					}
				}
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String reply = buffer.toString();
		return reply;
	}
}
