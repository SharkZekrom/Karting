package viewPackage;

import modelPackage.Customer;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CustomerInformationsModel extends AbstractTableModel {

    private Customer content;
    private ArrayList<String> columnNames;

    public CustomerInformationsModel(Customer customer) {
        columnNames = new ArrayList<>();
        columnNames.add("Email");
        columnNames.add("Locality");
        columnNames.add("Post code");
        columnNames.add("Address number");
        columnNames.add("Street");
        columnNames.add("First name");
        columnNames.add("Last name");
        columnNames.add("Birth date");
        columnNames.add("Gender");
        columnNames.add("Phone number");
        columnNames.add("Professional");
        setContent(customer);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    public void addRow(Object[] rowData) {
    }

    public void setRowCount(int i) {
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = content;
        switch (columnIndex) {
            case 0:
                return customer.getEmail();
            case 1:
                return customer.getLocality();
            case 2:
                return customer.getPostCode();
            case 3:
                return customer.getAddressNumber();
            case 4:
                return customer.getStreet();
            case 5:
                return customer.getFirstName();
            case 6:
                return customer.getLastName();
            case 7:
                return customer.getBirthDate();
            case 8:
                return customer.getGender();
            case 9:
                return customer.getPhoneNumber();
            case 10:
                return customer.isProfessional();
            default:
                return null;
        }
    }

    private void setContent(Customer customer) {
        this.content = customer;
    }

    public Class getColumnClass(int column) {
        Class c;
        switch (column) {
            case 0:
                c = String.class;
                break;
            case 1:
                c = String.class;
                break;
            case 2:
                c = String.class;
                break;
            case 3:
                c = Integer.class;
                break;
            case 4:
                c = String.class;
                break;
            case 5:
                c = String.class;
                break;
            case 6:
                c = String.class;
                break;
            case 7:
                c = String.class;
                break;
            case 8:
                c = String.class;
                break;
            case 9:
                c = String.class;
                break;
            case 10:
                c = Boolean.class;
                break;
            default:
                c = String.class;
        }
        return c;
    }
}
