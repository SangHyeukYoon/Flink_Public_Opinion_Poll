package domain.opinion;

public enum Region {

    SEOUL(0, "서울"),
    GYEONGGI(1, "경기"),
    CHUNGCHEONG(2, "충청"),
    GANGWON(3, "강원"),
    GYEONGNAM(4, "경남"),
    GYEONGBUK(5, "경북"),
    JEOLLA(6, "전라"),
    JEJU(7, "제주");

    private int key;
    private String regionStr;

    Region(int key, String regionStr) {
        this.key = key;
        this.regionStr = regionStr;
    }

    @Override
    public String toString() {
        return regionStr;
    }

    public static Region valueOf(int key) {
        for (Region region: Region.values()) {
            if (region.key == key) {
                return region;
            }
        }

        return null;    // TODO: Or should throw exception?
    }

}
