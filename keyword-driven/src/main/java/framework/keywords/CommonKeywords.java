package framework.keywords;

import framework.core.WebExecutor;
import framework.models.TestStep;

public class CommonKeywords {

    private WebExecutor web;

    public CommonKeywords(WebExecutor web) {
        this.web = web;
    }

    public void openUrl(TestStep step) {
        web.openUrl(step.getData());
    }

    public void click(TestStep step) {
        web.click(step.getLocatorType(), step.getLocatorValue());
    }

    public void type(TestStep step) {
        web.type(step.getLocatorType(), step.getLocatorValue(), step.getData());
    }

    public void assertText(TestStep step) {
        web.assertText(step.getLocatorType(), step.getLocatorValue(), step.getData());
    }
}
