package com.mikolka9144.WoCserver.utills.gzip;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import java.io.IOException;

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
    public byte[] getGzipedData(){
        return data;
    }
    public byte[] getData() {
        try {
            return GZipConverter.unGzip(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setData(byte[] data) {
        try {
            this.data = GZipConverter.Gzipify(data);
            header.setSize(this.data.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
