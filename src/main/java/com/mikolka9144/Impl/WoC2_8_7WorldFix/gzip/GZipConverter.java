package com.mikolka9144.Impl.WoC2_8_7WorldFix.gzip;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipConverter {
    public static List<GzipEntry> unGtar(byte[] data) throws IOException {
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new ByteArrayInputStream(unGzip(data)));
        List<GzipEntry> entries = new ArrayList<>();
        while (true){
            TarArchiveEntry header = tarIn.getNextTarEntry();
            if (header == null) break;
            ByteArrayOutputStream tarBytes = new ByteArrayOutputStream();
            IOUtils.copy(tarIn,tarBytes);
            entries.add(new GzipEntry(header,tarBytes.toByteArray()));
        }
        return entries;
    }
    public static byte[] Gtarify(List<GzipEntry> entries) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream gzip = new TarArchiveOutputStream(out);
        for (GzipEntry file : entries) {
            gzip.putArchiveEntry(file.getHeader());
            IOUtils.copy(new ByteArrayInputStream(file.getGzipedData()),gzip);
            gzip.closeArchiveEntry();
        }
        gzip.close();
        return Gzipify(out.toByteArray());
    }
    public static byte[] unGzip(byte[] data) throws IOException {
        GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        in.close();
        out.close();

        return out.toByteArray();
    }
    public static byte[] Gzipify(byte[] data) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        IOUtils.copy(in, gzip);
        in.close();
        gzip.close();

        return out.toByteArray();
    }
}
