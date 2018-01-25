package cn.dajiahui.kid.http.bean;

/**
 *
 */
public class BeDownFile {
    private String materialId;//资料id

    private int fileType; //文件类型
    private String fileUrl;//文件路径
    private String name; //文件名字
    private String locaUrl;//本地路径

    /*下载点读运品*/
    public BeDownFile( int fileType,String fileUrl, String name, String locaUrl) {
        this.fileType=fileType;
        this.fileUrl = fileUrl;
        this.name = name;
        this.locaUrl = locaUrl;

    }

    public BeDownFile(String materialId, int fileType, String fileUrl, String name) {
        this.materialId = materialId;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.name = name;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocaUrl() {
        return locaUrl;
    }

    public void setLocaUrl(String locaUrl) {
        this.locaUrl = locaUrl;
    }
}
