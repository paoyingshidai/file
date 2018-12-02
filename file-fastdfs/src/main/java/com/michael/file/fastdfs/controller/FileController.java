package com.michael.file.fastdfs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
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

//    ExecutorService executorService = Executors.newFixedThreadPool(20);

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



	@RequestMapping("multiFileUpload")
	public ModelAndView multiFileUpload(HttpServletRequest request, ModelAndView view) {

		TrackerServer ts = null;
		StorageServer ss = null;

		try {
			ts = ClientGlobal.g_tracker_group.getConnection();
			ss = tc.getStoreStorage(ts, "g1");
			sc = new StorageClient1(ts, ss);

			List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("fileName");
			List<String> paths = new ArrayList<>(10);

			long startTime = System.currentTimeMillis();
			for (MultipartFile file : files) {

				if (!file.isEmpty()) {
					Map<String, String> metaList = Maps.newHashMapWithExpectedSize(3);
					metaList.put("contentType", file.getContentType());
					metaList.put("filename", file.getOriginalFilename());

					String path = sc.upload_file1("g1", file.getBytes(), FilenameUtils.getExtension(file.getOriginalFilename()), metaList);
					paths.add(path);

				}
			}

			long endTime = System.currentTimeMillis();
			System.out.println("多文件执行时间(单线程)： " + (endTime - startTime));

			view.setViewName("success");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return view;
	}


    /**
     * 多线程会提高上传速度,但是频繁的创建客户端会消耗 cpu 资源
     * 现在 fastdfs 还没有多线程上传的 api
     * @param request
     * @param view
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
	@RequestMapping("multiFileUpload2")
	public ModelAndView multiFileUpload2(HttpServletRequest request, ModelAndView view) throws ExecutionException, InterruptedException {

		// 手动创建线程池(阿里巴巴规约)
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("multiImageUpload").build();
		ExecutorService executorService = new ThreadPoolExecutor(5, 20, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024), nameThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("fileName");
        List<Future<String>> results = new ArrayList<>(10);
        List<String> paths = new ArrayList<>(10);

        try {
            long startTime = System.currentTimeMillis();
            for (MultipartFile file : files) {
                Future<String> result = executorService.submit(() -> {

                    TrackerServer ts = ClientGlobal.g_tracker_group.getConnection();
                    StorageServer ss = tc.getStoreStorage(ts, "g1");
                    StorageClient1 sc1 = sc1 = new StorageClient1(ts, ss);

                    if (!file.isEmpty()) {
                        Map<String, String> metaList = Maps.newHashMapWithExpectedSize(3);
                        metaList.put("contentType", file.getContentType());
                        metaList.put("filename", file.getOriginalFilename());

                        String path = sc1.upload_file1("g1", file.getBytes(), FilenameUtils.getExtension(file.getOriginalFilename()), metaList);
                        return path;
                    } else {
                        return "";
                    }
                });
                results.add(result);
            }
            for (Future<String> re : results) {
                paths.add(re.get());
            }

            System.out.println(paths.size());

            long endTime = System.currentTimeMillis();
            System.out.println("多文件执行时间(多线程)：" + (endTime - startTime));

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.setViewName("success");
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
