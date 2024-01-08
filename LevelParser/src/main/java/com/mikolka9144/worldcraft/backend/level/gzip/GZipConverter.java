package com.mikolka9144.worldcraft.backend.level.gzip;

import com.mikolka9144.worldcraft.utills.exception.CompressionException;
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
    private static final int BUFFER_SIZE = 1000000; // 1Mb buffer should be enough

    private GZipConverter() {
    }

    public static List<GzipEntry> unGtar(byte[] data) throws CompressionException{
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new ByteArrayInputStream(unGzip(data)));
        List<GzipEntry> entries = new ArrayList<>();
        try {
            while (true) {
                TarArchiveEntry header = tarIn.getNextTarEntry();
                if (header == null) break;
                ByteArrayOutputStream tarBytes = new ByteArrayOutputStream();
                IOUtils.copy(tarIn, tarBytes);
                entries.add(new GzipEntry(header, tarBytes.toByteArray()));
            }
        }
        catch (IOException x){
            throw new CompressionException("Failed to extract nodes in tarball...",x);
        }
        return entries;
    }

    public static byte[] gtarify(List<GzipEntry> entries) throws CompressionException  {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream gzip = new TarArchiveOutputStream(out);
        try (gzip){
            for (GzipEntry file : entries) {
                gzip.putArchiveEntry(file.getHeader());
                IOUtils.copy(new ByteArrayInputStream(file.getData()), gzip);
                gzip.closeArchiveEntry();
            }
        }
        catch (IOException x){ throw new CompressionException("Failed to pack nodes in tarball...",x);}

        return gzipify(out.toByteArray());
    }


    public static byte[] unGzip(byte[] data) throws CompressionException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try(out) {
            GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(data));
            IOUtils.copy(in, out);
            in.close();
        }
        catch (IOException x){ throw new CompressionException("Failed to compress-stream from gzip...",x);}
        return out.toByteArray();
    }

    public static byte[] gzipify(byte[] data) throws CompressionException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try(in) {
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            IOUtils.copy(in, gzip);
            gzip.close();
        }
        catch (IOException x){ throw new CompressionException("Failed to compress-stream to gzip...",x);}
        return out.toByteArray();
    }

    public static byte[] zlibify(byte[] data) {

        byte[] output = new byte[BUFFER_SIZE];
        Deflater compresser = new Deflater();
        compresser.setInput(data);
        compresser.finish();
        int compressedDataLength = compresser.deflate(output);
        compresser.end();
        return Arrays.copyOf(output, compressedDataLength);
    }

    @SneakyThrows
    public static byte[] unZlib(byte[] data) {
        byte[] output = new byte[BUFFER_SIZE];
        Inflater decompresser = new Inflater();
        decompresser.setInput(data);
        int resultLength = decompresser.inflate(output);
        decompresser.end();
        return Arrays.copyOf(output, resultLength);
    }
}
