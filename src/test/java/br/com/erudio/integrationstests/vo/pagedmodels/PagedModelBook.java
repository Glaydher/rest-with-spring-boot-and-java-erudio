package br.com.erudio.integrationstests.vo.pagedmodels;

import java.io.Serializable;
import java.util.List;

import br.com.erudio.integrationstests.vo.BookVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelBook implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "content")
	private List<BookVO> content;

	public PagedModelBook() {
		super();
	}

	public List<BookVO> getContent() {
		return content;
	}

	public void setContent(List<BookVO> content) {
		this.content = content;
	}
	
	

}
