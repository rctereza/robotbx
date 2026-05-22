package com.rctereza.robotbx.tools;

import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.MaskFormatter;

public class TableUtils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public static DefaultTableCellRenderer getDateRenderer() {

		DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value instanceof Date) {
					setHorizontalAlignment(SwingConstants.CENTER);
					setText(sdf.format((Date) value));
				} else {
					setText("");
				}
				return this;
			}
		};
		return dateRenderer;
	}

	public static DefaultTableCellRenderer getDocumentRenderer() {

		DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value instanceof String) {
					setHorizontalAlignment(SwingConstants.CENTER);
					// String cnpjStr = String.format("%014d", cnpjInt);
					String formatted = "";
					try {
						MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
						mask.setValueContainsLiteralCharacters(false);
						formatted = mask.valueToString((String) value);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					setText(formatted);
				} else {
					setText("");
				}
				return this;
			}
		};
		return dateRenderer;
	}
}
