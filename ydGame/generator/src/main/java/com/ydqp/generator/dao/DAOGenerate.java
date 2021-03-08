package com.ydqp.generator.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cfq
 */
public class DAOGenerate {

    public static List<TableEntity> generateDAO(String connectionURL, String user, String password) {
        List<TableEntity> tableList = null;
        Connection con = JDBCConnection.getConnection(connectionURL, user, password);
        try {
            DatabaseMetaData databaseMetaData = con.getMetaData();
            ResultSet tableRet = databaseMetaData.getTables(null, "%", "%", new String[]{"TABLE"});
            Set<String> tableSet = new HashSet<String>();
            String table;
            String columnName;
            String primaryKeyColumnName;
            String columnType;
            String comment;
            while (tableRet.next()) {
                table = tableRet.getString("TABLE_NAME");
                if (!table.startsWith("seq_")) {
                    tableSet.add(table);
                }
            }
            tableList = new ArrayList<TableEntity>(tableSet.size());
            TableEntity tableEntity;
            ColumnEntity columnEntity;
            for (String tableName : tableSet) {
                tableEntity = new TableEntity();
                ResultSet colRet = databaseMetaData.getColumns(null, "%", tableName, "%");
                ResultSet primaryKeySet = databaseMetaData.getPrimaryKeys(null, null, tableName);
                while (colRet.next()) {
                    columnEntity = new ColumnEntity();
                    comment = colRet.getString("REMARKS").replaceAll("\r", " ").replaceAll("\n", " ").replaceAll("\"", "\\\\\"");
                    columnEntity.setComment(comment);
                    columnName = colRet.getString("COLUMN_NAME");
                    columnEntity.setColumnName(columnName);
                    columnType = colRet.getString("TYPE_NAME");
                    System.out.println("columnType=" + columnType);
                    columnEntity.setColumnType(columnType);
                    int datasize = colRet.getInt("COLUMN_SIZE");
                    columnEntity.setDataSize(datasize);
                    //int digits = colRet.getInt("DECIMAL_DIGITS");
                    //int nullable = colRet.getInt("NULLABLE");
                    while (primaryKeySet.next()) {
                        primaryKeyColumnName = primaryKeySet.getString("COLUMN_NAME");
                        if (primaryKeyColumnName.equals(columnName)) {
                            tableEntity.getPrimaryKeyColumnList().add(columnEntity);
                        }
                    }
                    tableEntity.getColumnList().add(columnEntity);
                }
                tableEntity.setTableName(tableName);
                tableList.add(tableEntity);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tableList;
    }
}
