package com.autoyol.service.impl;

import com.autoyol.dao.NoteBookMapper;
import com.autoyol.entity.NoteBook;
import com.autoyol.entity.Result;
import com.autoyol.service.NoteBookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NoteBookServiceImpl implements NoteBookService{
	@Resource
	private NoteBookMapper noteBookMapper;
	
	static{
//		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_ATSQL);
	}
	
	public Result loadBooks(String userId) {
		List<NoteBook> list = noteBookMapper.findBooks();
		
		for(NoteBook book:list){
			System.out.println(book.getNotebook_name());
		}
		
		Result result = new Result();
		result.setStatus(0);
		result.setMsg("success");
		result.setData(list);
		
		return result;
	}
	
}
