package viewPackage;

import modelPackage.Race;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SpeedKartInformationsModel extends AbstractTableModel {

    private List<Race> races;
    private String[] columnNames = {"id", "date", "startTime", "endTime", "category", "speedLimit"};

    public SpeedKartInformationsModel(List<Race> races) {
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
        Race race = races.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return race.getId();
            case 1:
                return race.getDate();
            case 2:
                return race.getStartTime();
            case 3:
                return race.getEndTime();
            case 4:
                return race.getCategory();
            case 5:
                return race.getCategory().getSpeedLimit();
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
            default:
                c = String.class;
        }
        return c;
    }
}
