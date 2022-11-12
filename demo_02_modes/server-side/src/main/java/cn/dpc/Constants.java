package cn.dpc;

import org.springframework.util.MimeType;

public class Constants {
    public static final MimeType CLIENT_ID_MIME = MimeType.valueOf("message/x.client.id");
    public static final String CLIENT_ID = "client.id";

    public static final MimeType METHOD_MIME = MimeType.valueOf("message/x.method");

    public static final String METHOD = "method";
}
