package com.mikolka9144.level.gzip;

import lombok.Getter;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

@Getter
public class GzipEntry {
    private TarArchiveEntry header;

    public GzipEntry(TarArchiveEntry header, byte[] data) {
        this.header = header;
        this.data = data;
    }

    private byte[] data;

    public void setHeader(TarArchiveEntry header) {
        this.header = header;
    }

    public void setData(byte[] data) {
            this.data = data;
            header.setSize(this.data.length);
    }
}
