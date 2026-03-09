package modals;

import java.io.Serializable;
import java.util.List;

public class Variant implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private List<Section> sections;
	private String price;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Section> getSections() {
		return sections;
	}
	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
