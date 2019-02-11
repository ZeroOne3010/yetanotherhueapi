package io.github.zeroone3010.yahueapi.domain;

public class ComponentSoftwareUpdate {
    private String state;
    private String lastinstall;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLastinstall() {
        return lastinstall;
    }

    public void setLastinstall(String lastinstall) {
        this.lastinstall = lastinstall;
    }

    @Override
    public String toString() {
        return JsonStringUtil.toJsonString(this);
    }
}
