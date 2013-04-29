package smartcampus.ifame.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ListaMense")
public class ListaMense {

	private List<Mensa> list;

	public ListaMense() {
	}

	public List<Mensa> getList() {
		return list;
	}

	public void setList(List<Mensa> list) {
		this.list = list;
	}

}
