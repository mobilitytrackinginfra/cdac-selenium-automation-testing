package framework.models;

import java.util.ArrayList;
import java.util.List;

public class TestCase {
    private String testCaseId;
    private List<TestStep> steps = new ArrayList<>();

    public TestCase(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public void addStep(TestStep step) {
        steps.add(step);
    }

    public List<TestStep> getSteps() {
        return steps;
    }

    public String getTestCaseId() {
        return testCaseId;
    }
}
