package modals;

import java.io.Serializable;
import java.util.List;

public class Car implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private List<Variant> variants;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Variant> getVariants() {
		return variants;
	}
	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}
	
}
