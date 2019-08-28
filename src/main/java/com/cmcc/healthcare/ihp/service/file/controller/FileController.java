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
@RequestMapping(value = "/dz",method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
public class FileController {
	
	@Value("${common.file.base.savepath}")
	private String fileBaseSavePath;
	
	/**
	 * 上传单个文件的相关信息,上传成功后，返回当前文件的http地址 
	 * @param file excel数据文件
	 * @param rate 年利率
	 * @param volume 回款次数
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public JSONObject uploadFile( @RequestPart(name="file") MultipartFile file,
					    		  @RequestParam(name="rate",required=true) double rate,
					              @RequestParam(name="volume",required=true) int volume ) throws Exception {		

		if (file == null || file.isEmpty()) {
			return ReturnUtil.ajaxDoneNullError("上传文件为空");
		}
		
		//文件全路径
		String filePath = "";
		String fileRealName  = file.getOriginalFilename();
		//文件类型
		String contentType = this.getFilenameSuffix(fileRealName);
		if (!"xlsx".equalsIgnoreCase(contentType)) {
			  return ReturnUtil.ajaxDone(ReturnUtil.CODE_FAIL, "上传的文件类型错误,只能上传xlsx格式文件。");
		}
		//转码后的文件
		String fileTranscodingName = CommonFileUtil.fileRename(fileRealName, "");
		
		//将客户端上传的文件存放在服务器指定的位置	
		//存储文件的文件夹				 
		String fileSaveDir = fileBaseSavePath + "/";	
		// 如果文件夹不存在，创建
		FileUtil.confirmFolderExists(fileSaveDir);
		// 保存文件
		InputStream fileInputStream = file.getInputStream();
		FileUtil.saveFile(fileInputStream, fileSaveDir, fileTranscodingName);
		fileInputStream.close();
		
		filePath = fileSaveDir + fileTranscodingName;
		// ResultBean resultBean = this.readExcel(rate, volume, filePath);
		return null;
	}
		
	
    /**
     * 检查类型通用方法
     */
    private boolean checkType(List<String> checkTypes, String filename){
        return checkTypes.contains(getFilenameSuffix(filename));
    }

    /**
     * 获取文件名称的后缀
     * @param filename
     * @return 文件后缀
     */
    private String getFilenameSuffix(String filename) {
        String suffix = null;
        if (StringUtils.isNotBlank(filename) && filename.contains(".")) {
            suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        }
        return suffix;
    }
    
 
      
}
