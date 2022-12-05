package cn.dpc;

public enum UploadStatus {
    CHUNK_COMPLETED, // 文件上传处理中
    COMPLETED, // 文件上传完成
    FAILED; // 文件上传失败
}
