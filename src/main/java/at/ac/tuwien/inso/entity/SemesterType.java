package at.ac.tuwien.inso.entity;

public enum SemesterType {

    WinterSemester("WS"),
    SummerSemester("SS");

    private final String name;

    SemesterType(String name) {
        this.name = name;
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
