package cn.itcast.web.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/user/upload")
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取文件上传的基准位置
        String basePath = this.getServletContext().getRealPath("/upload");
        //按日期对文件夹进行管理
        String datePath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //判断上传文件夹是否存在，不存在就创建一个
        File dir = new File(basePath + "/" + datePath);
        if (!dir.exists())
            dir.mkdirs();

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fileItems = null;
        try {
            //解析request,获取提交表单的所有表单项
            fileItems = upload.parseRequest(req);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        //遍历所有表单项
        for (FileItem fileItem : fileItems) {
            //如果该表单项是文件
            if (!fileItem.isFormField()) {
                //获取文件名
                String filename = fileItem.getName();
                //写入到磁盘中
                try {
                    fileItem.write(new File(dir,filename));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //文件大于10kb会产生临时文件，持久化完成后需要删除,小于10kb临时文件在缓存中
                fileItem.delete();
            }
        }

    }
}
