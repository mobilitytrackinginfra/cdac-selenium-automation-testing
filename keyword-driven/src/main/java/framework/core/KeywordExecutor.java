package framework.core;

import framework.keywords.CommonKeywords;
import framework.models.TestStep;

public class KeywordExecutor {

    private final CommonKeywords commonKeywords;

    public KeywordExecutor(WebExecutor webExecutor) {
        this.commonKeywords = new CommonKeywords(webExecutor);
    }

    public void executeStep(TestStep step) {
        if (step == null) {
            throw new IllegalArgumentException("TestStep cannot be null");
        }

        String action = step.getActionName();
        if (action == null) {
            throw new RuntimeException("Action name is null in step");
        }

        // normalize to avoid case issues like "Click", "CLICK", etc.
        String key = action.trim().toLowerCase();

        switch (key) {
            case "openurl":
                commonKeywords.openUrl(step);
                break;

            case "click":
                commonKeywords.click(step);
                break;

            case "type":
                commonKeywords.type(step);
                break;

            case "asserttext":
                commonKeywords.assertText(step);
                break;

            default:
                throw new RuntimeException("Unknown action: " + action);
        }
    }
}
