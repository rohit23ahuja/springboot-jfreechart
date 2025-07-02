package dev.rohitahuja.service;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChartUtils {

    private static final Logger _log = LoggerFactory.getLogger(ChartUtils.class);

    public static void logDefaultCategoryDataset(DefaultCategoryDataset dataset) {
        int rowCount = dataset.getRowCount();
        int colCount = dataset.getColumnCount();
        for (int r = 0; r < rowCount; r++) {
            Comparable<?> rowKey = dataset.getRowKey(r);
            for (int c = 0; c < colCount; c++) {
                Comparable<?> columnKey = dataset.getColumnKey(c);
                Number value = dataset.getValue(r, c);
                _log.info("Dataset value: [rowKey='{}', columnKey='{}'] = {}", rowKey, columnKey, value);
            }
        }
    }

    public static void logDefaultPieDataset(DefaultPieDataset dataset) {
        int keyCount = dataset.getItemCount();
        for (int i = 0; i < keyCount; i++) {
            Comparable<?> key = dataset.getKey(i);
            Number value = dataset.getValue(i);
            _log.info("PieDataset value: [key='{}'] = {}", key, value);
        }
    }

}
