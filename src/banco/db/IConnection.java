package banco.db;

import java.sql.Connection;

public interface IConnection {
	Connection getConnection();
    void closeConnection();
}
