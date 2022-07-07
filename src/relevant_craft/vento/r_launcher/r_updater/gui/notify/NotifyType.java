package relevant_craft.vento.r_launcher.r_updater.gui.notify;

public enum NotifyType {
    INFO("info.png"),
    ALERT("alert.png"),
    QUESTION("question.png"),
    ;

    private String name;

    NotifyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
