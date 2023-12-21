package br.com.erudio.integrationstests.vo.pagedmodels;

import java.io.Serializable;
import java.util.List;

import br.com.erudio.integrationstests.vo.PersonVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "content")
	private List<PersonVO> content;

	public PagedModelPerson() {
	}

	public List<PersonVO> getContent() {
		return content;
	}

	public void setContent(List<PersonVO> content) {
		this.content = content;
	}
	
	

}
