package land.sebastianwie.shopware_uploadtool.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import land.sebastianwie.shopware_uploadtool.resources.article.ArticleProperty;

public class MysqlConnector {
	Connection conn;

	/**
	 * Erstellt eine neue Verbindung mit einer MySQL-Datenbank.
	 * 
	 * @param hostname
	 * @param username
	 * @param password
	 * @param database
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public MysqlConnector(String hostname, String username, String password, String database) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Properties connProperties = new Properties();
		connProperties.put("user", username);
		connProperties.put("password", password);

		conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":3306/" + database, connProperties);
	}

	/**
	 * Schließt die Verbindung mit der Datenbank.
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		conn.close();
	}

	/**
	 * Setzt die ID des Custom Products für einen Artikel. Dies setzt das
	 * Shopware-Plugin "Custom Products" voraus.
	 * 
	 * @param articleId
	 * @param customGroupId
	 * @return
	 * @throws SQLException
	 */
	public int setCustomProduct(int articleId, int customGroupId) throws SQLException {
		if (customGroupId < 0)
			return deleteCustomProduct(articleId);
		Statement s = conn.createStatement();
		int updateResult = s.executeUpdate("update s_plugin_customizing_articles set group_id=" + customGroupId + " where article_id="
				+ articleId);
		if (updateResult == 0)
			return s.executeUpdate("insert into s_plugin_customizing_articles(article_id, group_id) values(" + articleId + ", "
					+ customGroupId + ")");
		else
			return updateResult;
	}

	/**
	 * Fragt die ID des Custom Producst für einen Artikel ab. Dies setzt das
	 * Shopware-Plugin "Custom Products" voraus.
	 * 
	 * @param articleId
	 * @return
	 * @throws SQLException
	 */
	public int getCustomProductId(int articleId) throws SQLException {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select group_id from s_plugin_customizing_articles where article_id=" + articleId);
		if (rs.next())
			return rs.getInt("group_id");
		else
			return -1;
	}

	/**
	 * Löscht den Eintrag des Custom Products
	 * 
	 * @param articleId
	 * @return
	 * @throws SQLException
	 */
	public int deleteCustomProduct(int articleId) throws SQLException {
		Statement s = conn.createStatement();
		return s.executeUpdate("delete from s_plugin_customizing_articles where article_id=" + articleId);
	}

	/**
	 * Frägt Name und Wert einer Eigenschaft bei einer gegeben ID ab.
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public ArticleProperty getPropertyById(int id) throws SQLException {
		Statement s = conn.createStatement();
		// @f:off
		String query = "select " + "v.id, o.name, v.value " + "from s_filter_values v " + "left join "
				+ "s_filter_options o on v.optionId = o.id " + "where v.id = " + id;
		// @f:on
		ResultSet rs = s.executeQuery(query);
		if (rs.next())
			return new ArticleProperty(rs.getString("name"), rs.getString("value"));
		else
			return null;
	}

	/**
	 * Frägt Kategorienamen für eine gegebene ID ab.
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public String getCategoryNameById(int id) throws SQLException {
		Statement s = conn.createStatement();
		String query = "select " + "description " + "from s_categories " + "where id = " + id;
		ResultSet rs = s.executeQuery(query);
		if (rs.next())
			return rs.getString("description");
		else
			return null;
	}

	/**
	 * Frägt Bildnamen für eine gegebene ID ab.
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public String getImageNameByMediaId(int id) throws SQLException {
		Statement s = conn.createStatement();
		String query = "select " + "img, extension " + "from s_articles_img " + "where media_id = " + id;
		ResultSet rs = s.executeQuery(query);
		if (rs.next())
			return rs.getString("img") + "." + rs.getString("extension");
		else
			return null;
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		String dbhost = "db1156.mydbserver.com";
		String dbuser = "p223131d1";
		String dbpassword = "oVoqecel!526";
		String dbdatabase = "usr_p223131_1";
		MysqlConnector c = new MysqlConnector(dbhost, dbuser, dbpassword, dbdatabase);

		// Statement stm = c.conn.createStatement();
		// ResultSet rs =
		// stm.executeQuery("select * from s_articles order by id desc limit 3");
		// while (rs.next()) {
		// System.out.println("id = " + rs.getInt("id"));
		// java.lang.ClassNotFoundException: com.mysql.jdbc.Driver}

		// System.out.println(c.getPropertyById(18));
		// System.out.println(c.getPropertyById(1));

		System.out.println(c.getCustomProductId(8203));

		c.closeConnection();

	}

}
