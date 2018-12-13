package cc.zolux.hcf.faction;

public enum FactionRole {

    LEADER, MOD, MEMBER;

    private int id = -1;

    public int getId() {
        if (id == -1) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i] == this) {
                    this.id = i;
                }
            }
            return id;
        }
        return 0;
    }
}
