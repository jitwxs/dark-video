package jit.wxs.dv.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jitwxs
 * @since 2018/6/16 21:02
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    /**
     * ffmpeg路径
     */
    private static String ffmpegPath = "D:/ffmpeg/bin/ffmpeg.exe";

    /**
     * 获取指定路径下所有目录
     *
     * @author jitwxs
     * @since 2018/6/16 21:03
     */
    public static List<File> listDir(String path) {
        File file = new File(path);
        List<File> list = new ArrayList<>();
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()) {
                    list.add(files[i]);
                }
            }
        }
        return list;
    }

    /**
     * 获取后缀名
     *
     * @author jitwxs
     * @since 2018/6/17 13:30
     */
    public static String getSuffix(String fileName) {
        if (!fileName.contains(".")) {
            return "";
        }
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        suffix = suffix.toLowerCase();
        return suffix;
    }

    public static String getPrefix(String fileName) {
        if (!fileName.contains(".")) {
            return "";
        }
        String prefix = fileName.substring(0, fileName.lastIndexOf("."));
        return prefix;
    }

    /**
     * 获取文件修改时间
     *
     * @author jitwxs
     * @since 2018/6/17 17:16
     */
    public static Date getModifiedTime(File f) {
        long time = f.lastModified();
        return new Date(time);
    }

    /**
     * 截取视频帧至指定目录
     * @param videoFile 源视频文件路径
     * @param frameFile 截取帧的图片存放路径
     * @param duration 视频时长
     * @param resolution 视频分辨率 形如960x568
     * @throws Exception
     */
    public static void fetchFrame(String videoFile, String frameFile, Integer duration, String resolution) {
        List<String> commands = new ArrayList<>();
        commands.add(ffmpegPath);
        commands.add("-ss");
        //这个参数是设置截取视频多少秒时的画面，截取视频的1/4位置
        int time = duration / 4;
        commands.add(time + "");
        commands.add("-i");
        commands.add(videoFile);
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        commands.add("-t");
        commands.add("0.001");
        commands.add("-s");
        //这个参数是设置截取图片的大小，等比例设置缩略图为800 * ? 的大小
        int height = Integer.parseInt(resolution.split("x")[1]) * 800 / Integer.parseInt(resolution.split("x")[0]);
        commands.add("800x" + height);
        commands.add(frameFile);

        try {
            ProcessBuilder builder = new ProcessBuilder();
            //执行命令
            builder.command(commands);
            builder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调整图片大小
     * @param srcImgPath  原图片路径
     * @param distImgPath 转换大小后图片路径
     * @param width       转换后图片宽度
     * @param height      转换后图片高度
     * @author jitwxs
     * @since 2018/6/17 21:54
     */
    public static void resizeImage(String srcImgPath, String distImgPath, int width, int height) throws IOException {
        File srcFile = new File(srcImgPath);
        Image srcImg = ImageIO.read(srcFile);
        BufferedImage buffImg;
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(
                srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
                0, null);

        ImageIO.write(buffImg, "JPEG", new File(distImgPath));

    }

    /**
     * 根据比例调整图片大小
     * @param srcImgPath  原图片路径
     * @param distImgPath 转换大小后图片路径
     * @param width       转换后图片宽度
     * @author jitwxs
     * @since 2018/8/12 17:09
     */
    public static void resizeImage(String srcImgPath, String distImgPath, int width) throws IOException  {
        File srcFile = new File(srcImgPath);
        Image srcImg = ImageIO.read(srcFile);

        BufferedImage buffImg;

        int height = width * ((BufferedImage) srcImg).getHeight() / ((BufferedImage) srcImg).getWidth();

        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(
                srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
                0, null);

        ImageIO.write(buffImg, "JPEG", new File(distImgPath));
    }

    /**
     * 格式化文件大小
     * @param size 文件大小：字节
     * @return 格式化后大小
     * @author jitwxs
     * @since 2018/8/29 19:59
     */
    public static String formatSize(long size) {
        DecimalFormat df = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "Byte";
        } else if (size < 1024 * 1024) {
            float kSize = size / 1024f;
            return df.format(kSize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mSize = size / 1024f / 1024f;
            return df.format(mSize) + "MB";
        } else if (size < 1024L * 1024L * 1024L * 1024L) {
            float gSize = size / 1024f / 1024f / 1024f;
            return df.format(gSize) + "GB";
        } else {
            return "size: error";
        }
    }

    /**
     * 获取视频信息
     * @return
     *      duration, 167 整型
     *      frameRate,30 fps 字符串
     *      createTime,2018-08-08 01:21:10 Date类型
     *      bitrate,1657kb/s 字符串
     *      durationStr,00:02:47 字符串
     *      resolution,960x568 字符串
     * @author jitwxs
     * @since 2018/8/29 21:53
     */
    public static Map<String, Object> getVideoInfo(String videoPath) {
        Map<String, Object> map = new HashMap<>(16);

        List<String> commands = new java.util.ArrayList<>();
        commands.add(ffmpegPath);
        commands.add("-i");
        commands.add(videoPath);

        StringBuilder sb = new StringBuilder();
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commands);

        try {
            final Process p = builder.start();
            //从输入流中读取视频信息
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = sb.toString();

        // 计算时长和比特率
        String regexDuration = "Duration: (.*?), bitrate: (\\d*) kb\\/s";
        Pattern pattern = Pattern.compile(regexDuration);
        Matcher m = pattern.matcher(data);
        if (m.find()) {
            String durationStr = m.group(1).split("\\.")[0];
            map.put("duration", parserTime(durationStr));
            map.put("durationStr", durationStr);
            map.put("bitrate", m.group(2)+"kb/s");
        }

        // 计算创建时间
        try {
            int createTimeStart = data.indexOf("creation_time");
            // 如果没有创建时间的数据，使用File的修改时间代替
            if(createTimeStart == -1) {
                map.put("createTime", getModifiedTime(new File(videoPath)));
            } else {
                String tmp = data.substring(createTimeStart);
                int createTimeEnd = tmp.indexOf("Stream");
                String createTimeStr = tmp.substring(0, createTimeEnd);
                String createTime = createTimeStr.substring(createTimeStr.indexOf(":")+1).trim();
                createTime = createTime.split(" ")[0];
                //注意是空格+UTC
                createTime = createTime.replace("Z", " UTC");
                //注意格式化的表达式
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                map.put("createTime", format.parse(createTime));
            }
        } catch (Exception e) {e.printStackTrace();}

        // 计算分辨率和帧率
        try {
            String regexResolution = "(\\d+)x(\\d+) \\[";
            pattern = Pattern.compile(regexResolution);
            m = pattern.matcher(data);
            if (m.find()) {
                String res = m.group(0).split(" ")[0];
                map.put("resolution", res);
            } else {
                regexResolution = "(\\d+)x(\\d+),";
                pattern = Pattern.compile(regexResolution);
                m = pattern.matcher(data);
                if (m.find()) {
                    map.put("resolution", m.group(0).substring(0,m.group(0).length()-1));
                }
            }

            String regexFrameRate = "(\\d)\\.*(\\d*) fps";
            pattern = Pattern.compile(regexFrameRate);
            m = pattern.matcher(data);
            if (m.find()) {
                map.put("frameRate", m.group(0));
            }
        } catch (Exception e) {e.printStackTrace();}

        return map;
    }

    /**
     * 格式:"00:00:10.68"
     * @author jitwxs
     * @since 2018/8/29 22:05
     */
    private static int parserTime(String time){
        int min=0;
        String strS[] = time.split(":");
        if (strS[0].compareTo("0") > 0) {
            min+=Integer.valueOf(strS[0])*60*60;//秒
        }
        if(strS[1].compareTo("0")>0){
            min+=Integer.valueOf(strS[1])*60;
        }
        if(strS[2].compareTo("0")>0){
            min+=Math.round(Float.valueOf(strS[2]));
        }
        return min;
    }

    public static void main(String[] args){

    }
}
