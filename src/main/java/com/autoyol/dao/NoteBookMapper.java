package com.autoyol.dao;

import com.autoyol.entity.NoteBook;

import java.util.List;


public interface NoteBookMapper {
	public List<NoteBook> findBooks();
}
