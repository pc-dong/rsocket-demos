package cn.dpc;

import lombok.Data;
@Data
public class StatusReport {
    public StatusReport(String status) {
        this.status = status;
    }

    public StatusReport() {
    }

    private String status;
}
