package stsc.database.migrations;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

final class DatafeedSettings {

	enum DatabaseType {
		production, development, test;
	}

	private final String jdbcDriver;
	private final String jdbcUrl;

	public DatafeedSettings() throws IOException {
		this(DatafeedSettings.class.getResourceAsStream("../../../feedzilla_test.properties"), DatabaseType.test);
	}

	public DatafeedSettings(InputStream sourceInputStream, DatabaseType type) throws IOException {
		try (DataInputStream inputStream = new DataInputStream(sourceInputStream)) {
			final Properties properties = new Properties();
			properties.load(inputStream);
			jdbcDriver = properties.getProperty("jdbc.driver");
			jdbcUrl = properties.getProperty("jdbc.url");
		}
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void migrate() throws SQLException, LiquibaseException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
		final String path = DatafeedSettings.class.getResource("../../../db.changelog.xml").getFile();
		final File parentPath = new File(path).getParentFile().getParentFile().getParentFile();
		final Liquibase liquibase = new Liquibase(path, new FileSystemResourceAccessor(parentPath.getAbsolutePath()), database);
		liquibase.update((String) null);
		liquibase.validate();
		c.close();
	}

}