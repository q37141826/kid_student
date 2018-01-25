package com.fxtx.framework.file;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import com.fxtx.framework.text.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * File 管理工具类
 */
public class FileUtil {
    /**
     * 根据路径获取一个在sd卡上的目录文件 ，没有则创建
     *
     * @param path
     */
    public File makeDirFile(String path) {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile;
    }


    /**
     * 根据路径获取一个在sd卡上的文件 ，没有则创建
     *
     * @param path
     */
    public File creatNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            String dirPath = StringUtil.getStrBeforeSplitter(path,
                    File.separator);
            try {
                makeDirFile(dirPath);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     * 根据路径获取一个在sd卡上的文件 ，没有则创建
     *
     * @param dirPath
     * @param fileName
     */
    public File creatNewFile(String dirPath, String fileName) {
        return creatNewFile(makeDirFile(dirPath), fileName);
    }

    public File creatNewFile(File dirFile, String fileName) {
        return new File(dirFile, fileName);
    }

    /**
     * 删除sd卡上的某一个文件
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file != null && file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * 根据路径删除sd卡上的某一个文件
     *
     * @param path
     */
    public void deleteFile(String path) {
        deleteFile(new File(path));
    }

    /**
     * 递归删除某一个路径下的所有文件
     *
     * @param dirPath
     */
    public void deleteAllFile(String dirPath) {
        deleteAllFile(new File(dirPath));
    }

    public void deleteAllFile(File dirFile) {
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isFile()) {
            dirFile.delete();
            return;
        }
        if (dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            if (files == null || files.length == 0) {
                dirFile.delete();
                return;
            }
            for (File file : files) {
                deleteAllFile(file);
            }
            dirFile.delete();
        }
    }


    /**
     * 保存信息到sd卡
     */
    public void saveData(String path, String data) {
        File file = creatNewFile(path);
        if (file != null) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 读取sd卡上文件信息
     */
    public String getData(String path) {
        String data = "";
        File file = creatNewFile(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            data = new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 将文件大小 进行格式化转换
     *
     * @param fileS
     * @return
     */
    public String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    //获取DirFile文件夹
    public String dirFile(Context context) {
        String dirFile;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            dirFile = Environment.getExternalStorageDirectory().toString();//获取跟目录
        } else {
            dirFile = context.getFilesDir().toString();
        }
        return dirFile;
    }



    /**
     * 从sd卡获取图片资源
     * @return
     */
    public List<String> getImagePathFromSD(String filePath) {
        // 图片列表
        List<String> imagePathList = new ArrayList<String>();
        // 得到sd卡内image文件夹的路径   File.separator(/)
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        if(files!=null){
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        }
        // 返回得到的图片列表
        return imagePathList;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     * @param fName  文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    /**
     * 复制文件
     *
     * @param fromFile 要复制的文件目录
     * @param toFile   要粘贴的文件目录
     * @return 是否复制成功
     */
    public static boolean copy(String fromFile, String toFile) {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return false;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            } else//如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return true;
    }
    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public static boolean CopySdcardFile(String fromFile, String toFile) {

        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }
    /*判断文件是否存在*/
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
