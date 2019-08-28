package com.cmcc.healthcare.ihp.service.file.controller;

import java.io.InputStream;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.healthcare.ihp.service.utility.CommonFileUtil;
import com.cmcc.healthcare.ihp.service.utility.FileUtil;
import com.cmcc.healthcare.ihp.service.utility.ReturnUtil;

@Controller
@RequestMapping(value = "/dz", produces = "application/json; charset=UTF-8")
public class TestController {
	
	
	/**
	 * 上传单个文件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/single")
    public String listFileByBussId() throws Exception {
		return "/uploadSingleFile";
	}
	
	/**
	 * 上传多个文件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/more")
    public String uploadMoreFiles() throws Exception {
		return "/uploadMoreFiles";
	}
	
}
