package relevant_craft.vento.r_launcher.r_updater.manager.java;

public class JavaFile {

    private String url;
    private int size;

    public JavaFile(String url, int size) {
        this.url = url;
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public int getSize() {
        return size;
    }
}
