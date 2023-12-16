package com.mikolka9144.worldcraft.level.gzip;

import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

public class GZipConverter {
    private GZipConverter(){}
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
    public static byte[] gtarify(List<GzipEntry> entries) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream gzip = new TarArchiveOutputStream(out);
        for (GzipEntry file : entries) {
            gzip.putArchiveEntry(file.getHeader());
            IOUtils.copy(new ByteArrayInputStream(file.getData()),gzip);
            gzip.closeArchiveEntry();
        }
        gzip.close();
        return gzipify(out.toByteArray());
    }
    @SneakyThrows
    public static byte[] unGzip(byte[] data)  {
        GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        in.close();
        out.close();

        return out.toByteArray();
    }
    public static byte[] gzipify(byte[] data) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        IOUtils.copy(in, gzip);
        in.close();
        gzip.close();

        return out.toByteArray();
    }
    public static byte[] zlibify(byte[] data){
        byte[] output = new byte[data.length+200]; //TODO might break
        Deflater compresser = new Deflater();
        compresser.setInput(data);
        compresser.finish();
        int compressedDataLength = compresser.deflate(output);
        compresser.end();
        return Arrays.copyOf(output,compressedDataLength);
    }
    @SneakyThrows
    public static byte[] unZlib(byte[] data){
        byte[] output = new byte[1000000]; // 1Mb buffer should be enough
        Inflater decompresser = new Inflater();
        decompresser.setInput(data);
        int resultLength = decompresser.inflate(output);
        decompresser.end();
        return Arrays.copyOf(output,resultLength);
    }
}
