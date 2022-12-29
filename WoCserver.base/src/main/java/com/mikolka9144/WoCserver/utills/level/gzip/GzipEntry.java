package com.mikolka9144.WoCserver.utills.level.gzip;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

public class GzipEntry {
    private TarArchiveEntry header;

    public GzipEntry(TarArchiveEntry header, byte[] data) {
        this.header = header;
        this.data = data;
    }

    private byte[] data;

    public TarArchiveEntry getHeader() {
        return header;
    }

    public void setHeader(TarArchiveEntry header) {
        this.header = header;
    }
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
            this.data = data;
            header.setSize(this.data.length);
    }
}
