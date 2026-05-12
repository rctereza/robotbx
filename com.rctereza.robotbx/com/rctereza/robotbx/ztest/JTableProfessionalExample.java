package com.rctereza.robotbx.ztest;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

public class JTableProfessionalExample extends JFrame {

	private static final long serialVersionUID = 359761140021687449L;

	// ==========================================
    // CLASS WITH 3 FIELDS
    // ==========================================
    static class Person {

        private int id;
        private String name;
        private int age;

        public Person(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    // ==========================================
    // CUSTOM TABLE MODEL
    // ==========================================
    static class PersonTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 5508898942706645608L;

		private final String[] columns = {
                "ID",
                "Name",
                "Age"
        };

        private final List<Person> people;

        public PersonTableModel(List<Person> people) {
            this.people = people;
        }

        @Override
        public int getRowCount() {
            return people.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            Person person = people.get(rowIndex);

            switch (columnIndex) {

                case 0:
                    return person.getId();

                case 1:
                    return person.getName();

                case 2:
                    return person.getAge();

                default:
                    return null;
            }
        }

        // Makes cells editable
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {

            // ID not editable
            return columnIndex != 0;
        }

        // Updates object when user edits table
        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {

            Person person = people.get(rowIndex);

            switch (columnIndex) {

                case 1:
                    person.setName(value.toString());
                    break;

                case 2:
                    person.setAge(Integer.parseInt(value.toString()));
                    break;
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        }

        // Add new row
        public void addPerson(Person person) {

            people.add(person);

            int lastRow = people.size() - 1;

            fireTableRowsInserted(lastRow, lastRow);
        }

        // Remove row
        public void removePerson(int rowIndex) {

            people.remove(rowIndex);

            fireTableRowsDeleted(rowIndex, rowIndex);
        }

        // Access original object
        public Person getPerson(int rowIndex) {
            return people.get(rowIndex);
        }
    }

    // ==========================================
    // GUI
    // ==========================================
    public JTableProfessionalExample() {

        setTitle("Professional JTable Example");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create object list
        List<Person> people = new ArrayList<>();

        people.add(new Person(1, "John", 25));
        people.add(new Person(2, "Mary", 30));
        people.add(new Person(3, "Peter", 22));

        // Create model
        PersonTableModel model = new PersonTableModel(people);

        // Create table
        JTable table = new JTable(model);

        // Optional table settings
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        // Add button action
        addButton.addActionListener(e -> {

            Person newPerson = new Person(
                    people.size() + 1,
                    "New Person",
                    20
            );

            model.addPerson(newPerson);
        });

        // Remove button action
        removeButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow >= 0) {
                model.removePerson(selectedRow);
            }
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new JTableProfessionalExample().setVisible(true);
        });
    }
}