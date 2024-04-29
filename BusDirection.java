public enum BusDirection {
    EAST, WEST;

    public String toString() {
        switch (this) {
            case EAST:
                return "E";
            case WEST:
                return "W";
            default:
                return "";
        }
    }
}
