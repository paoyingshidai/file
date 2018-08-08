package com.michael.file.fastdfs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.michael.file.fastdfs.object.File;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

}
