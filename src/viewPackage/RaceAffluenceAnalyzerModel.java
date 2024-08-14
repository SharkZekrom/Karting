package viewPackage;

import modelPackage.Race;
import modelPackage.TrackReservation;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RaceAffluenceAnalyzerModel extends AbstractTableModel {

    private List<TrackReservation> races;
    private String[] columnNames = {"Track name", "Number of reservations"};

    public RaceAffluenceAnalyzerModel(List<TrackReservation> races) {
        this.races = races;
    }

    @Override
    public int getRowCount() {
        return races.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TrackReservation trackReservation = races.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return trackReservation.getTrackName();
            case 1:
                return trackReservation.getReservationCount();
            default:
                return null;
        }
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
            default:
                c = String.class;
        }
        return c;
    }
}
