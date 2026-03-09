package modals;

import java.io.Serializable;
import java.util.Map;

public class Section implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sectionName;
	private Map<String, String> specs;
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public Map<String, String> getSpecs() {
		return specs;
	}
	public void setSpecs(Map<String, String> specs) {
		this.specs = specs;
	}
	
}
