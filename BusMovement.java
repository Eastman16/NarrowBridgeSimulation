public enum BusMovement {
    BOARDING, GOING_TO__THE_BRIDGE, WAITING_BEFORE_ENTERING, RIDING_ON_THE_BRIDGE, GOING_TO_THE_PARKING, UNLOADING;

    @Override
    public String toString() {
        switch (this){
            case BOARDING:
                return "BOARDING";
            case GOING_TO__THE_BRIDGE:
                return  "GOING_TO__THE_BRIDGE";
            case WAITING_BEFORE_ENTERING:
                return "WAITING_BEFORE_ENTERING";
            case RIDING_ON_THE_BRIDGE:
                return "RIDING_ON_THE_BRIDGE";
            case GOING_TO_THE_PARKING:
                return "GOING_TO_THE_PARKING";
            case UNLOADING:
                return "UNLOADING";
        }
        return "";
    }
}
