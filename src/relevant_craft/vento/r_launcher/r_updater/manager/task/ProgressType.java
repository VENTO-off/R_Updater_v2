package relevant_craft.vento.r_launcher.r_updater.manager.task;

public enum ProgressType {
    RUNNING("loading.png"),
    WARNING("warning.png"),
    SUCCESSFUL("check.png"),
    ;

    private String name;

    ProgressType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
