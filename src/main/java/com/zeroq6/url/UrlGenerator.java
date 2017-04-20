package com.zeroq6.url;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by icgeass on 2016/11/20.
 */


public class UrlGenerator {


    public static void main(String[] args) throws Exception {
        if (null == args || (args.length != 2 && args.length != 3)) {
            System.out.println("Usage: java -jar <jarfile> <webRoot> <baseDirectory> [charset]");
            return;
        }
        String os = System.getProperty("os.name", null);
        if(null == os){
            System.out.println("Error: could not get os.name property");
            return;
        }
        // windows 不区分大小写替换webRoot有问题且没有实际需要
        if(os.toLowerCase().indexOf("windows") != -1){
            System.out.println("Error: Unsupported OS: windows");
            return;
        }
        StringBuffer result = new StringBuffer();
        //
        File webRoot = new File(args[0]);
        File baseDirectory = new File(args[1]);
        String netIp = getRealIp();
        Charset charset = Charset.defaultCharset();
        if(args.length == 3){
            String charsetName = args[2];
            if(!Charset.isSupported(charsetName)){
                System.err.println("Error: Unsupported Charset: " + charsetName);
                return;
            }
            charset =  Charset.forName(args[2]);
        }
        System.out.println("Info: webRoot: " + webRoot.getAbsolutePath()); // getCanonicalPath会将返回软连接指向的实际路径
        System.out.println("Info: baseDirectory: " + baseDirectory.getAbsolutePath());
        System.out.println("Info: netIp: " + netIp);
        System.out.println("Info: charset: " + charset.name());
        if (!webRoot.exists() || !baseDirectory.exists() || !webRoot.isDirectory() || !baseDirectory.isDirectory()) {
            System.err.println("Error: webRoot or baseDirectory must be a directory");
            return;
        }
        if (!baseDirectory.getAbsolutePath().startsWith(webRoot.getAbsolutePath())) {
            System.err.println("Error: baseDirectory must be in webRoot");
            return;
        }
        System.out.println("Info: generating...");
        listFiles(baseDirectory, webRoot, result, netIp, charset);
        File desFile = new File("./url." + baseDirectory.getName() + "." + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
        System.out.println("Info: writing...");
        FileUtils.write(desFile, result.toString(), charset.name(), false);
        System.out.println("Info: success, result save to " + desFile.getCanonicalPath());

    }

    private static void listFiles(File f, File webRoot, StringBuffer result, String netIp, Charset charset) throws Exception {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (null != files) {
                for (File file : files) {
                    listFiles(file, webRoot, result, netIp, charset);
                }
            }
            return;
        }
        String url = f.getAbsolutePath().replace(webRoot.getAbsolutePath(), "");
        if (url.startsWith(File.separator)) {
            url = url.substring(File.separator.length());
        }
        StringBuffer stringBuffer = new StringBuffer();
        String[] splits = "\\".equals(File.separator) ? url.split("\\\\") : url.split(File.separator);
        for (int i = 0; i < splits.length; i++) {
            stringBuffer.append(URLEncoder.encode(splits[i], charset.name()).replace("+", "%20"));
            if (i != splits.length - 1) {
                stringBuffer.append(File.separator);
            }
        }
        url = String.format("http://%s/%s", netIp, stringBuffer.toString()).replace("\\", "/");
        result.append(url).append("\r\n");
    }

    private static String getRealIp() throws Exception {
        String localIp = "127.0.0.1";
        String netIp = null;
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                InetAddress ip = address.nextElement();
                if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    if (!ip.isSiteLocalAddress()) {
                        netIp = ip.getHostAddress();
                        break;
                    } else {
                        localIp = ip.getHostAddress();
                    }
                }
            }
        }
        if (null != netIp && netIp.trim().length() != 0) {
            return netIp;
        } else {
            return localIp;
        }

    }

}

