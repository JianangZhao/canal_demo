package org.example.test;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * when testing, should stop UserHandler consume the binlog
 */
@Component
public class CanalListener {

        public void printCanalLog(){
            //获取canal连接对象
            CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                    new InetSocketAddress("localhost", 11111), "example", "root", "0813");
            int i = 0;
            while(i <= 100){
                //获取连接
                canalConnector.connect();
                //指定要监控的数据库
                canalConnector.subscribe("canal_demo.*");
                //获取message 参数为每次获取100
                Message message = canalConnector.get(100);
                //一条数据对应一条entry
                List<CanalEntry.Entry> entries = message.getEntries();
                if (entries.size()<=0){
                    System.out.println("没有数据休息一会");
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    i++;
                    for (CanalEntry.Entry entry : entries) {
                        //获取表名
                        String tableName = entry.getHeader().getTableName();
                        // Entry 类型
                        CanalEntry.EntryType entryType = entry.getEntryType();
                        // 判断entryType 是否是ROWDATA
                        if (CanalEntry.EntryType.ROWDATA.equals(entryType)){
                            //序列化数据
                            ByteString storeValue = entry.getStoreValue();
                            //反序列化
                            CanalEntry.RowChange rowChange = null;
                            try {
                                rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                            } catch (InvalidProtocolBufferException e) {
                                throw new RuntimeException(e);
                            }
                            //获取事件类型
                            CanalEntry.EventType eventType = rowChange.getEventType();
                            //获取具体数据
                            List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
                            //遍历数据
                            for (CanalEntry.RowData rowData : rowDatasList) {
                                List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                                Map<String, Object> bMap = new HashMap<>();
                                for (CanalEntry.Column column : beforeColumnsList) {
                                    bMap.put(column.getName(), column.getValue());
                                }
                                Map<String, Object> aMap = new HashMap<>();
                                List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                                for (CanalEntry.Column column : afterColumnsList) {
                                    aMap.put(column.getName(), column.getValue());
                                }
                                System.out.println("表名：" + tableName + "操作类型：" + eventType);
                                System.out.println("改前：" + bMap);
                                System.out.println("改后：" + aMap);
                            }

                        }
                    }
                }
            }
        }

}
