package com.michael.file.fastdfs.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.michael.file.fastdfs.object.File;
import com.michael.file.fastdfs.repository.FileRepository;

@Controller
public class FileController {


	@Autowired
	private TrackerClient tc;

	private StorageClient1 sc;

	@Autowired
	private FileRepository fileRepository;

	@RequestMapping("index")
	public String index() {

		return "/index";
	}

	@RequestMapping("fileUpload")
	public ModelAndView fileUpload(@RequestParam("fileName") MultipartFile file, ModelAndView view) {

		TrackerServer ts = null;
  		StorageServer ss = null;

  		try {
			ts = ClientGlobal.g_tracker_group.getConnection();
			ss = tc.getStoreStorage(ts, "g1");
			sc = new StorageClient1(ts, ss);

			Map<String, String> metaList = Maps.newHashMapWithExpectedSize(3);
			metaList.put("contentType", file.getContentType());
			metaList.put("filename", file.getOriginalFilename());

			String path = sc.upload_file1("g1", file.getBytes(), FilenameUtils.getExtension(file.getOriginalFilename()), metaList);

			System.out.println(path);
			File myfile = File.builder().path(path).fileName(file.getOriginalFilename()).build();
			fileRepository.saveAndFlush(myfile);

			view.addObject("filePath", path);
			view.setViewName("success");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return view;
	}

	@RequestMapping("fileDownload")
	public ResponseEntity<byte[]> fuleDownload(HttpServletResponse response) throws Exception {


		TrackerServer ts = null;
  		StorageServer ss = null;

		ts = ClientGlobal.g_tracker_group.getConnection();
		ss = tc.getStoreStorage(ts, "g1");
		sc = new StorageClient1(ts, ss);

		byte[] filebyte = sc.download_file("g1", "g1/M00/00/00/wKjmgVtpxY6AHa-_AAAM53U0amM052.jpg");

		return new ResponseEntity<byte[]>(filebyte, HttpStatus.OK);
	}

}
