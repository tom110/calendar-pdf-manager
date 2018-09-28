package com.tom.sdgm.newspaper.controller;

import com.tom.sdgm.newspaper.domain.TPDF;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Controller
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/")
    public String index() {
        return "pdfviewer";
    }


    @RequestMapping(value = "/getPdfs",produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String getPdfs() {
        JSONArray jsonArray=getFiles();
        return jsonArray.toString();
    }

    @RequestMapping(value = "/deletePDF")
    public @ResponseBody String deletePDF(@RequestParam("title") String title,@RequestParam("start") String start) {
            if(deleteDir(new File(System.getProperties().getProperty("user.home") + "\\newspapers\\"+start))){
                return "删除成功";
            }else{
                return "删除失败";
            }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    //上传文件
    @RequestMapping(value = "/acceptService", method = RequestMethod.POST)
    public String acceptService(HttpServletRequest httpServletRequest,
                                @RequestParam("date") Date date,
                                @RequestParam("soureFile") MultipartFile[] files) {
        String filePath = "";
        //判断file数组不能为空并且长度大于0
        if (files != null && files.length > 0) {
            //循环获取file数组中得文件
            filePath = saveFile(files[0], httpServletRequest, date);
        }

        return "redirect:/";
    }

    //得到文件目录
    private JSONArray getFiles() {
        JSONArray jsonArray=new JSONArray();
        File file=new File(System.getProperties().getProperty("user.home") + "\\newspapers\\");
        if(file.exists()){
            File[] files=file.listFiles();
            for(File f :files){
                File file_pdf=new File(f.getAbsolutePath());
                if(file_pdf.exists()){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("title",file_pdf.list()[0]);
                    jsonObject.put("start",f.getName());
                    jsonObject.put("end",f.getName());
                    jsonArray.put(jsonObject);
                }
            }
        }
        return jsonArray;
    }

    //保存文件
    private String saveFile(MultipartFile file, HttpServletRequest request, Date date) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                String dateString=simpleDateFormat.format(date);

                // 文件保存路径
                String filePath = System.getProperties().getProperty("user.home") + "\\newspapers\\" + dateString;
                // 转存文件
                File dir = new File(filePath);
                if (!dir.exists()) {// 判断目录是否存在
                    dir.mkdirs();
                }

                String filename = file.getOriginalFilename();
                filePath = filePath + "\\" + filename.substring(filename.lastIndexOf("\\") + 1);

                file.transferTo(new File(filePath));

                return filePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @RequestMapping(value="/returnPDF", produces = "application/pdf")
    @ResponseBody
    public FileSystemResource returnPDF(@RequestParam("title") String title,@RequestParam("start") String start){
        return new FileSystemResource(System.getProperties().getProperty("user.home") + "\\newspapers\\"+start+"\\"+title);
    }

    @RequestMapping("/viewPDF")
    public String viewPDF() {
        return "index";
    }
}
