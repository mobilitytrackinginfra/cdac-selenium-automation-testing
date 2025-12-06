package framework.models;

public class TestStep {

    private String actionName;
    private String locatorType;
    private String locatorValue;
    private String data;

    public TestStep(String actionName,String locatorType,String locatorValue,String data) {
        this.actionName = actionName;
        this.locatorType = locatorType;
        this.locatorValue = locatorValue;
        this.data = data;
    }

    public String getActionName() {
        return actionName;
    }

    public String getLocatorType() {
        return locatorType;
    }

    public String getLocatorValue() {
        return locatorValue;
    }

    public String getData() {
        return data;
    }
}
