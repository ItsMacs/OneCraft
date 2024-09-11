package eu.macsworks.fiverr.onecraft.utils;

import com.mysql.cj.jdbc.Driver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.macsworks.fiverr.onecraft.OneCraft;
import org.bukkit.Bukkit;

import java.sql.*;

public class DataSource {

	private final HikariDataSource ds;

	public DataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://" + OneCraft.getInstance().getMacsPluginLoader().getSqlHost());
		config.setUsername( OneCraft.getInstance().getMacsPluginLoader().getSqlUser());
		config.setPassword( OneCraft.getInstance().getMacsPluginLoader().getSqlPassword());
		config.addDataSourceProperty( "cachePrepStmts" , "true" );
		config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
		config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
		config.setDriverClassName(Driver.class.getName());
		config.setMaximumPoolSize(500);
		config.setConnectionTimeout(1000);
		config.setLeakDetectionThreshold(10000);
		ds = new HikariDataSource(config);

		//Create time table
		Bukkit.getScheduler().runTaskAsynchronously(OneCraft.getInstance(), () -> {
			try(Connection conn = getConnection()){
				try(PreparedStatement statement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `ONECRAFT_TIME` (`time` VARCHAR(1) PRIMARY KEY UNIQUE, `val` LONG);")){
					statement.execute();
				}catch (SQLException e){
					e.printStackTrace();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		});
	}

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public void setTime(long time){
		Bukkit.getScheduler().runTaskAsynchronously(OneCraft.getInstance(), () -> {
			try(Connection connection = ds.getConnection()){
				try(PreparedStatement statement = connection.prepareStatement("REPLACE INTO ONECRAFT_TIME (time, val) VALUES (?, ?);")) {
					statement.setString(1, "t");
					statement.setLong(2, time);
					statement.execute();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		});
	}
}