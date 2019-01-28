package com.autoyol.controller;

import com.autoyol.entity.Result;
import com.autoyol.service.NoteBookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/notebook")
public class NoteBookController {
	@Resource
	private NoteBookService noteBookService;
	
	@RequestMapping("/loadbooks")
	@ResponseBody
	public Result loadbooks(String userId){
		Result noteResult = noteBookService.loadBooks(userId);
		return noteResult;
	}
}
