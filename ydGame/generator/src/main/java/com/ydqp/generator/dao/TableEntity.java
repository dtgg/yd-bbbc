
package com.ydqp.generator.dao;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cfq
 */
public class TableEntity {
    private String tableName;
    private List<ColumnEntity> primaryKeyColumnList = new ArrayList<ColumnEntity>();
    private List<ColumnEntity> columnList = new ArrayList<ColumnEntity>();

    /**
     * @return the primaryKeyColumnList
     */
    public List<ColumnEntity> getPrimaryKeyColumnList() {
        return primaryKeyColumnList;
    }

    /**
     * @param primaryKeyColumnList the primaryKeyColumnList to set
     */
    public void setPrimaryKeyColumnList(List<ColumnEntity> primaryKeyColumnList) {
        this.primaryKeyColumnList = primaryKeyColumnList;
    }

    /**
     * @return the columnList
     */
    public List<ColumnEntity> getColumnList() {
        return columnList;
    }

    /**
     * @param columnList the columnList to set
     */
    public void setColumnList(List<ColumnEntity> columnList) {
        this.columnList = columnList;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
}
