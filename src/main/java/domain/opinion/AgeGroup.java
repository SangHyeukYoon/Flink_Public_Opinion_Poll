package domain.opinion;

public enum AgeGroup {

    TWENTY(0, "20대", 20),
    THIRTY(1, "30대", 30),
    FORTY(2, "40대", 40),
    FIFTY(3, "50대", 50),
    SIXTY(4, "60대 이상", 60);


    private int key;
    private String ageGroupStr;
    private int ageGroup;

    AgeGroup(int key, String ageGroupStr, int ageGroup) {
        this.key = key;
        this.ageGroupStr = ageGroupStr;
        this.ageGroup = ageGroup;
    }

    public int getAgeGroup() {
        return ageGroup;
    }

    @Override
    public String toString() {
        return ageGroupStr;
    }

    public static AgeGroup valueOf(int key) {
        for (AgeGroup ageGroup: AgeGroup.values()) {
            if (ageGroup.key == key) {
                return ageGroup;
            }
        }

        return null;    // TODO: Or should throw exception?
    }

}
