package viewPackage;

import modelPackage.Customer;
import modelPackage.Track;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TrackInformationsModel extends AbstractTableModel {

    private Track content;
    private ArrayList<String> columnNames;


    public TrackInformationsModel(Track track) {
        columnNames = new ArrayList<>();
        columnNames.add("Label");
        columnNames.add("Length");
        columnNames.add("Is official");
        columnNames.add("Address number");
        columnNames.add("Street");
        setContent(track);
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
        Track track = content;
        switch (columnIndex) {
            case 0:
                return track.getLabel();
            case 1:
                return track.getLength();
            case 2:
                return track.isOfficial();
            case 3:
                return track.getAddress().getAddressNumber();
            case 4:
                return track.getAddress().getAddressStreetLabel();
            default:
                return null;
        }
    }

    private void setContent(Track customer) {
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
            default:
                c = String.class;
        }
        return c;
    }
}