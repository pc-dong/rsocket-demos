package cn.dpc.config;

import org.springframework.util.MimeType;

public class Constants {
    public static final MimeType CLIENT_ID_MIME = MimeType.valueOf("message/x.client.id");
    public static final String CLIENT_ID = "client.id";

    public static final MimeType FILE_NAME_MIME = MimeType.valueOf("message/x.upload.file.name");
    public static final String FILE_NAME= "file.name";

    public static final MimeType FILE_EXT_MIME = MimeType.valueOf("message/x.upload.file.ex");
    public static final String FILE_EXT= "file.ext";

}
