package modelPackage;

public class TrackReservation {
    private String trackName;
    private int reservationCount;

    public TrackReservation(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackName() {
        return trackName;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void addReservation() {
        reservationCount++;
    }
}
