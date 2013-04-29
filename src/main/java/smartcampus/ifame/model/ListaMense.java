package smartcampus.ifame.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ListaMense")
public class ListaMense {

	private List<Mensa> list;
	private Long last_update_milis;

	public ListaMense() {
	}

	public List<Mensa> getList() {
		return list;
	}

	public void setList(List<Mensa> list) {
		this.list = list;
	}

	public Long getLast_update_milis() {
		return last_update_milis;
	}

	public void setLast_update_milis(Long last_update_milis) {
		this.last_update_milis = last_update_milis;
	}

}
