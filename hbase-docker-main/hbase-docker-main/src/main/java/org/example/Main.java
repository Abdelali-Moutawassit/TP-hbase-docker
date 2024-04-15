package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;

public class Main {
    public static final String TABLE_NAME = "users";
    public static final String CF_INFO = "info";
    public static final String CF_GRADES = "grades";
    public static void afficher(Result result) {
        for(Cell cell: result.rawCells()) {
            byte[] cf = CellUtil.cloneFamily(cell);
            byte[] qualifier = CellUtil.cloneQualifier(cell);
            byte[] value = CellUtil.cloneValue(cell);

            String cfString = Bytes.toString(cf);
            String qualifierString = Bytes.toString(qualifier);
            String valueString = Bytes.toString(value);

            System.out.println("Column Family: " + cfString +
                    ", Column: " + qualifierString +
                    ", Value: " + valueString);
        }
    }


    public static void main(String[] args) {

     Configuration config = HBaseConfiguration.create();
        config.set ("hbase.zookeeper.quorum", "zookeeper"); // Le nom du service Zookeeper dans Docker Compose
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set ("hbase.master", "hbase-master:16000"); // Le nom du service master et le port exposé
        try
        {
            Connection connection = ConnectionFactory.createConnection(config);
            Admin admin = connection.getAdmin();

            TableName tableName = TableName.valueOf(TABLE_NAME);
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
            tableDescriptorBuilder.setColumnFamily(ColumnFamilyDescriptorBuilder.of(CF_INFO));
            tableDescriptorBuilder.setColumnFamily(ColumnFamilyDescriptorBuilder.of(CF_GRADES));

            TableDescriptor tableDescriptor = tableDescriptorBuilder.build();

            if (!admin.tableExists(tableName)) {
                admin.createTable(tableDescriptor);
                System.out.println("Tableau Cree!");
                System.out.println("Le nom de tableau : " + tableName.getNameAsString());
            } else  {
                System.err.println("Tableau deja existe!");
            }

            Table table = connection.getTable(tableName);
            Put put = new Put(Bytes.toBytes("student-1"));
            put.addColumn(Bytes.toBytes(CF_INFO), Bytes.toBytes("name"), Bytes.toBytes("Oussama"));
            put.addColumn(Bytes.toBytes(CF_INFO), Bytes.toBytes("age"), Bytes.toBytes("21"));
            put.addColumn(Bytes.toBytes(CF_GRADES), Bytes.toBytes("math"), Bytes.toBytes("B"));
            put.addColumn(Bytes.toBytes(CF_GRADES), Bytes.toBytes("science"), Bytes.toBytes("A"));
            table.put(put);
            put = new Put(Bytes.toBytes("student-2"));
            put.addColumn(Bytes.toBytes(CF_INFO), Bytes.toBytes("name"), Bytes.toBytes("Elhachimi"));
            put.addColumn(Bytes.toBytes(CF_INFO), Bytes.toBytes("age"), Bytes.toBytes("30"));
            put.addColumn(Bytes.toBytes(CF_GRADES), Bytes.toBytes("math"), Bytes.toBytes("A"));
            put.addColumn(Bytes.toBytes(CF_GRADES), Bytes.toBytes("science"), Bytes.toBytes("A"));
            table.put(put);
            System.out.println("Vous avez ajouter deux etudiants !");

            Get get = new Get(Bytes.toBytes("student-1"));
            Result result = table.get(get);
            afficher(result);

            put = new Put(Bytes.toBytes("student-1"));
            put.addColumn(Bytes.toBytes(CF_INFO), Bytes.toBytes("name"), Bytes.toBytes("Oussama"));
            put.addColumn(Bytes.toBytes(CF_INFO), Bytes.toBytes("age"), Bytes.toBytes("23"));
            put.addColumn(Bytes.toBytes(CF_GRADES), Bytes.toBytes("math"), Bytes.toBytes("A+"));
            table.put(put);

            System.out.println("Vous avez bien modifier !");

            get = new Get(Bytes.toBytes("student-1"));
            result = table.get(get);

            afficher(result);

            Delete delete = new Delete(Bytes.toBytes("student-1"));
            table.delete(delete);

            System.out.println("Vous avez supprimer la ligne !");

            admin.disableTable(tableName);
            admin.deleteTable(tableName);

            System.out.println("Vous avez supprimer le tableau !");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}