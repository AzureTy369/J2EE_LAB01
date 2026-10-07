package com.example.bootstrap.persistence.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.bootstrap.persistence.model.Book;

// Spring Data JPA tự sinh phần cài đặt: save, findById, findAll, deleteById, ...
public interface BookRepository extends CrudRepository<Book, Long> {

    // Câu truy vấn được sinh từ tên phương thức: SELECT b FROM Book b WHERE b.title = ?1
    List<Book> findByTitle(String title);
}
