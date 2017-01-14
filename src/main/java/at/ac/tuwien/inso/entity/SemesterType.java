package at.ac.tuwien.inso.entity;


public enum SemesterType {

    WinterSemester("WS", 10, 1),
    SummerSemester("SS", 3, 1);

    /**
     * Name of the semester: WS or SS
     */
    private final String name;

    /**
     * Month the semester starts
     */
    private final int startMonth;

    /**
     * Day in month the semester starts
     */
    private final int startDay;


    SemesterType(String name, int startMonth, int startDay) {
        this.name = name;
        this.startMonth = startMonth;
        this.startDay = startDay;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Reverse of toString
     */
    public static SemesterType fromString(String name) {
        for (SemesterType type : SemesterType.values()) {
            if (type.toString().equals(name)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Type '" + name + "' is not a valid SemesterType");
    }
}
